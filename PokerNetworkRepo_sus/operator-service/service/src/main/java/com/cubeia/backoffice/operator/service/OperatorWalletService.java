/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.backoffice.operator.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cubeia.backoffice.accounting.api.NegativeBalanceException;
import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.BalanceResponse;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.DepositResponse;
import com.cubeia.backoffice.operator.api.OperatorConfigParamDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestEventStatus;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogEventDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestQuery;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestQueryResult;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestStatus;
import com.cubeia.backoffice.operator.api.WalletDTO;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.api.WithdrawResponse;
import com.cubeia.backoffice.operator.client.OperatorWalletClient;
import com.cubeia.backoffice.operator.service.dao.OperatorWalletDAO;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLog;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLogEvent;
import com.cubeia.backoffice.operator.service.entity.RequestStatus;
import com.cubeia.backoffice.operator.service.wallet.AuditLog;
import com.cubeia.backoffice.operator.service.wallet.OperatorWalletClientFactory;
import com.cubeia.backoffice.operator.service.wallet.UserLookup;
import com.cubeia.backoffice.operator.service.wallet.WalletClientFactory;
import com.cubeia.backoffice.operator.wallet.exceptions.InsufficientFundsException;
import com.cubeia.backoffice.operator.wallet.exceptions.NotFoundException;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class OperatorWalletService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired OperatorService operatorService;

    @Autowired OperatorWalletClientFactory clientFactory;

    @Autowired AuditLog audit;

    @Autowired TransferService transferService;

    @Autowired WalletClientFactory walletClientFactory;

    WalletServiceClient walletClient;
    
    @Autowired UserLookup userLookup;

    @Autowired OperatorWalletDAO operatorWalletDao;

    /**
     * Caches remote operator clients for a certain time. 
     */
    private LoadingCache<Long, OperatorWalletClient> clients = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, OperatorWalletClient>() {
                public OperatorWalletClient load(Long operatorId) {
                    return createClient(operatorId);
                }
            });

    public BalanceResponse getBalance(Long operatorId, BalanceRequest request) {
        log.debug("Get balance for player: "+request);
        OperatorWalletClient client = getOperatorWalletClient(operatorId);
        injectExternalUserId(request);
        BalanceResponse balance = null;
        try {
            balance = client.getBalance(request);
        } catch (Exception e) {
            audit.auditBalanceRequestFailed(operatorId, request, e.getMessage());
            throw e;
        }
        return balance;
    }

    public WithdrawResponse withdraw(Long operatorId, WithdrawRequest request) {
        log.debug("Player withdraw: "+request);
        Long logId = audit.auditNewRequestLog(operatorId, request);
        OperatorWalletClient client = getOperatorWalletClient(operatorId);
        injectExternalUserId(request);
        WithdrawResponse withdraw = null;
        try {
            withdraw = client.withdraw(request);
            audit.auditOperatorTransferCompleted(logId);
        } catch (InsufficientFundsException e) {
            audit.auditDenied(logId);
            throw e;
        } catch (Exception e) {
            log.warn("Operator wallet withdraw request failed. Request["+request+"]", e);
            audit.auditFailed(logId, e.getMessage());
            throw e;
        }

        // We have successfully withdrawn money from the operator. Make the internal accounting call.
        try {
            doTransfer(operatorId, request.getUserId(), request.getFunds().getCurrency(), request.getFunds().getAmount(), true, logId);
        } catch (NegativeBalanceException e) {
            audit.auditInternalFail(logId, "Withdraw amount exceeds operator account limit. OpId["+operatorId+"] PID["+request.getUserId()+"] Amount["+request.getFunds().getAmount()+"] " +
                    "Currency["+request.getFunds().getCurrency()+"] WithdrawFromOperator["+true+"]");
            throw e;
        }
        audit.auditInternalTransferCompleted(logId);
        
        audit.auditCompleted(logId, withdraw);
        return withdraw;
    }

    

    public DepositResponse deposit(Long operatorId, DepositRequest request) {
        log.debug("Player deposit: "+request);
        Long logId = audit.auditNewRequestLog(operatorId, request);
        OperatorWalletClient client = getOperatorWalletClient(operatorId);
        injectExternalUserId(request);
        DepositResponse deposit = null;

        // Do internal transfer before we do the remote transfer so we don't try to send more money than is actually in the
        // user's account.
        try {
            doTransfer(operatorId, request.getUserId(), request.getFunds().getCurrency(), request.getFunds().getAmount(), false, logId);
        } catch (NegativeBalanceException e) {
            audit.auditDeniedInternally(logId, "Withdraw amount exceeds user account limit. OpId["+operatorId+"] PID["+request.getUserId()+"] Amount["+request.getFunds().getAmount()+"] " +
            		"Currency["+request.getFunds().getCurrency()+"] WithdrawFromOperator["+false+"]");
            throw e;
        }
        audit.auditInternalTransferCompleted(logId);
        
        try {
            deposit = client.deposit(request);
            audit.auditOperatorTransferCompleted(logId);
        } catch (Exception e) {
            log.warn("Operator wallet withdraw request failed. Request["+request+"]", e);
            audit.auditFailed(logId, e.getMessage());
            throw e;
        }

        audit.auditCompleted(logId, deposit);
        return deposit;
    }

    public OperatorWalletRequestQueryResult findWalletRequestLogEntries(OperatorWalletRequestQuery query) {
        return findWalletRequestLogEntries(query.getOperatorId(), query.getStatuses(), query.getQueryOffset(), query.getQueryLimit(), query.getOrder(), query.isAscending());
    }

    @Transactional
    public OperatorWalletRequestQueryResult findWalletRequestLogEntries(Long operatorId, Collection<OperatorWalletRequestStatus> statuses, int offset, int limit, OperatorWalletRequestOrder order, boolean ascending) {
        log.debug("find wallet request log entries: operatorId = {}, statuses = {}, offset = {}, limit = {}, order = {}, ascending = {}", 
                new Object[] {operatorId, statuses, new Integer(offset), new Integer(limit), order, ascending});

        Collection<RequestStatus> entityStatuses = null;
        if (statuses != null) {
            entityStatuses = new ArrayList<>();
            for (OperatorWalletRequestStatus status : statuses) {
                log.debug("Statuses size: "+statuses.size());
                log.debug("Status: "+status);
                log.debug("Status.name: "+status.name());
                log.debug("entityStatuses: "+entityStatuses);
                entityStatuses.add(RequestStatus.valueOf(status.name()));
            }
        }

        int size = operatorWalletDao.countLogEntries(operatorId, entityStatuses);
        List<OperatorWalletRequestLog> foundLogEntries = operatorWalletDao.findLogEntries(operatorId, entityStatuses, offset, limit, order, ascending);

        List<OperatorWalletRequestLogDTO> entries = new ArrayList<>();
        for (OperatorWalletRequestLog entity : foundLogEntries) {
            entries.add(translate(entity));
        }

        log.debug("returning {} wallet request log entries, total query result size = {}", foundLogEntries.size(), size);
        return new OperatorWalletRequestQueryResult(offset, limit, size, entries, order, ascending);
    }
    
    public OperatorWalletRequestLogDTO findWalletRequestLogById(Long walletRequestId) {
        log.debug("Lookup Wallet Request by ID: "+walletRequestId);
        OperatorWalletRequestLog entity = operatorWalletDao.findById(OperatorWalletRequestLog.class, walletRequestId);
        log.debug("Found entity: "+entity);
        return translate(entity);
    }

    @VisibleForTesting
    protected OperatorWalletRequestLogDTO translate(OperatorWalletRequestLog entity) {
        OperatorWalletRequestLogDTO dto = new OperatorWalletRequestLogDTO();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setOperatorId(entity.getOperatorId());
        dto.setPayload(entity.getPayload());
        dto.setRequestId(entity.getRequestId());
        dto.setRequestStatus(OperatorWalletRequestStatus.valueOf(entity.getStatus().name()));
        dto.setResponse(entity.getResponse());
        dto.setTimestamp(entity.getTimestamp());
        dto.setTimestampMillis(entity.getTimestampMillis());
        dto.setType(entity.getType());
        dto.setAmount(entity.getAmount());
        dto.setCurrency(entity.getCurrency());
        
        for (OperatorWalletRequestLogEvent e : entity.getEvents()) {
            OperatorWalletRequestLogEventDTO event = new OperatorWalletRequestLogEventDTO();
            event.setId(e.getId());
            event.setMessage(e.getMessage());
            event.setNewStatus(OperatorWalletRequestEventStatus.valueOf(e.getNewStatus().name()));
            event.setTimestamp(e.getTimestamp());
            event.setTimestampMillis(e.getTimestampMillis());
            
            dto.getEvents().add(event);
        }
        
        dto.setRequestStatus(OperatorWalletRequestStatus.valueOf(entity.getStatus().name()));
        return dto;
    }

    private void doTransfer(Long operatorId, Long userId, String currency, BigDecimal amount, boolean withdrawFromOperator, Long auditLogId) {
        try {
            transferService.transferCubeiaAccounting(operatorId, userId, currency, amount, withdrawFromOperator);
        } catch (NotFoundException e) {
            audit.auditInternalFail(auditLogId, "Account missing. OpId["+operatorId+"] PID["+userId+"]. Reported error: "+e+", "+e.getMessage());
            throw e;
        } catch (NegativeBalanceException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Internal transfer failed for player request. OpId["+operatorId+"] PID["+userId+"] Amount["+amount+"] Currency["+currency+"] withdrawFromOperator["+withdrawFromOperator+"].", e);
            audit.auditInternalFail(auditLogId, e+". Message: "+e.getMessage());
            throw e;
        }
    }
    
    private void injectExternalUserId(WalletDTO request) {
        if (request.getUserId() == null) {
            log.warn("User id cannot be null: "+request);
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
        String externalId = userLookup.getExternalId(request.getUserId());
        request.setExternalUserId(externalId);
    }

    protected OperatorWalletClient createClient(Long operatorId) {
        String apiKey = operatorService.getOperatorConfig(operatorId, OperatorConfigParamDTO.API_KEY);
        String walletServiceURL = operatorService.getOperatorConfig(operatorId, OperatorConfigParamDTO.WALLET_SERVICE_ENDPOINT_URL);
        log.info("Create new Operator Wallet client with API-KEY["+apiKey+"] WalletURL["+walletServiceURL+"]");
        return clientFactory.create(apiKey, walletServiceURL);
    }

    private OperatorWalletClient getOperatorWalletClient(Long operatorId) {
        try {
            return clients.get(operatorId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


}

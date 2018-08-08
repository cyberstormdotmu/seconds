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
package com.cubeia.backoffice.operator.service.wallet;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cubeia.backoffice.operator.api.AbstractWalletResponse;
import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.WalletDTO;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.service.dao.OperatorWalletDAO;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLog;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLogEvent;
import com.cubeia.backoffice.operator.service.entity.RequestEventStatus;
import com.cubeia.backoffice.operator.service.entity.RequestStatus;

/**
 * Writes an audit log in form of a request object and events coupled to the request object.
 * 
 * This is quite write-heavy on the database and may prove to be difficult to scale up 
 * properly. If this becomes a bottleneck then consider scaling out by partioning by log entry id, or 
 * even let each web server have they own database schema.
 * 
 * @author Fredrik
 */
@Component
public class AuditLog {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired OperatorWalletDAO dao;
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Transactional
    public Long auditNewRequestLog(Long operatorId, WalletDTO request) {
        log.debug("Create new request log entry");
        String payload;
        try {
            payload = mapper.writeValueAsString(request);
            payload = truncate(payload);
        } catch (Exception e) {
            throw new RuntimeException("Operator Wallet Request could not be serialized to JSON for Audit purposes.", e);
        }
        
        OperatorWalletRequestLog entry = new OperatorWalletRequestLog();
        entry.setOperatorId(operatorId);
        entry.setRequestId(request.getRequestId());
        entry.setPayload(payload);
        entry.setType(request.getClass().getSimpleName());
        
        if (request instanceof WithdrawRequest) {
            WithdrawRequest withdraw = (WithdrawRequest) request;
            entry.setAmount(withdraw.getFunds().getAmount().toString());
            entry.setCurrency(withdraw.getFunds().getCurrency());
        } else if (request instanceof DepositRequest) {
            DepositRequest deposit = (DepositRequest) request;
            entry.setAmount(deposit.getFunds().getAmount().toString());
            entry.setCurrency(deposit.getFunds().getCurrency());
        }
        
        addEvent(entry, RequestEventStatus.CREATED, null);
        
        dao.save(entry);
        return entry.getId();
    }
    
    @Transactional
    public void auditCompleted(Long id, AbstractWalletResponse response) {
        String responseValue = null;
        try {
            responseValue = mapper.writeValueAsString(response);
            responseValue = truncate(responseValue);
        } catch (Exception e) {
            log.error("Could not serialize wallet response from operator for audit purposes. Response: "+response, e);
        }
        
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, id);
        entry.setResponse(responseValue);
        entry.setStatus(RequestStatus.COMPLETED);
        
        addEvent(entry, RequestEventStatus.COMPLETED, null);
    }

    @Transactional
    public void auditDenied(Long id) {
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, id);
        entry.setStatus(RequestStatus.DENIED);
        addEvent(entry, RequestEventStatus.DENIED_BY_OPERATOR, null);
    }
    
    public void auditDeniedInternally(Long id, String message) {
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, id);
        entry.setStatus(RequestStatus.DENIED);
        addEvent(entry, RequestEventStatus.DENIED_INTERNALLY, message);
    }
    
    @Transactional
    public void auditFailed(Long id, String message) {
        log.debug("Request failed: "+id+", "+message);
        message = truncate(message);
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, id);
        entry.setMessage(message);
        entry.setStatus(RequestStatus.FAILED);
        
        addEvent(entry, RequestEventStatus.FAILED, message);
    }
    
    @Transactional
    public void auditBalanceRequestFailed(Long operatorId, BalanceRequest request, String message) {
        message = truncate(message);
        String payload = null;
        try {
            payload = mapper.writeValueAsString(request);
        } catch (Exception e) {
            log.error("Could not serialize get balance request for audit purposes. Request: "+request, e);
        }
        
        OperatorWalletRequestLog entry = new OperatorWalletRequestLog();
        entry.setOperatorId(operatorId);
        entry.setRequestId(request.getRequestId());
        entry.setPayload(payload);
        entry.setType(request.getClass().getSimpleName());
        entry.setMessage(message);
        entry.setStatus(RequestStatus.FAILED);
        
        addEvent(entry, RequestEventStatus.FAILED, message);
        
        dao.save(entry);
    }
    
    @Transactional
    public void auditInternalFail(Long id, String message) {
        message = truncate(message);
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, id);
        entry.setMessage(message);
        entry.setStatus(RequestStatus.FAILED);
        
        addEvent(entry, RequestEventStatus.INTERNAL_TRANSFER_FAILED, message);
    }

    private void addEvent(OperatorWalletRequestLog entry, RequestEventStatus newStatus, String message) {
        OperatorWalletRequestLogEvent event = new OperatorWalletRequestLogEvent(entry, newStatus, message);
        entry.getEvents().add(event);
    }
    
    private String truncate(String value) {
        if (value != null && value.length() > 255) {
            value = value.substring(0, 255);
        }
        return value;
    }

    @Transactional
    public void auditInternalTransferCompleted(Long logId) {
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, logId);
        addEvent(entry, RequestEventStatus.INTERNAL_TRANSFER_COMPLETED, null);
    }
    
    @Transactional
    public void auditOperatorTransferCompleted(Long logId) {
        OperatorWalletRequestLog entry = dao.findById(OperatorWalletRequestLog.class, logId);
        addEvent(entry, RequestEventStatus.OPERATOR_TRANSFER_COMPLETED, null);
    }


}

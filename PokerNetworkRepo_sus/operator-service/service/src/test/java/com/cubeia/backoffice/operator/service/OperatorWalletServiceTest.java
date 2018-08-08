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

import static junit.framework.Assert.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.cubeia.backoffice.accounting.api.NegativeBalanceException;
import com.cubeia.backoffice.operator.api.AbstractWalletResponse;
import com.cubeia.backoffice.operator.api.BalanceRequest;
import com.cubeia.backoffice.operator.api.DepositRequest;
import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestLogDTO;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestQueryResult;
import com.cubeia.backoffice.operator.api.OperatorWalletRequestStatus;
import com.cubeia.backoffice.operator.api.WalletDTO;
import com.cubeia.backoffice.operator.api.WithdrawRequest;
import com.cubeia.backoffice.operator.client.OperatorWalletClient;
import com.cubeia.backoffice.operator.service.dao.OperatorWalletDAO;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLog;
import com.cubeia.backoffice.operator.service.entity.RequestStatus;
import com.cubeia.backoffice.operator.service.wallet.AuditLog;
import com.cubeia.backoffice.operator.service.wallet.OperatorWalletClientFactory;
import com.cubeia.backoffice.operator.service.wallet.UserLookup;
import com.cubeia.backoffice.operator.wallet.exceptions.InsufficientFundsException;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;

public class OperatorWalletServiceTest {

    OperatorWalletService service;
    
    @Mock OperatorWalletClient client;
    
    @Mock OperatorService operatorService;
    
    @Mock OperatorWalletClientFactory clientFactory;
    
    @Mock WalletServiceClient walletClient;
    
    @Mock AuditLog audit;
    
    @Mock TransferService transferService;
    
    @Mock OperatorWalletDAO operatorWalletDao;
    
    @Mock UserLookup userLookup;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new OperatorWalletService();
        service.clientFactory = clientFactory;
        service.operatorService = operatorService;
        service.audit = audit;
        service.walletClient = walletClient;
        service.transferService = transferService;
        service.operatorWalletDao = operatorWalletDao;
        service.userLookup = userLookup;
        
        when(clientFactory.create(Mockito.anyString(), Mockito.anyString())).thenReturn(client);
        when(audit.auditNewRequestLog(Mockito.anyLong(), Mockito.any(WalletDTO.class))).thenReturn(1L);
        
    }
    
    @Test
    public void testGetBalance() {
        BalanceRequest request = createBalance();
        service.getBalance(1L, request );
        
        verify(clientFactory).create(Mockito.anyString(), Mockito.anyString());
        verify(client).getBalance(Mockito.any(BalanceRequest.class));
        
    }
    
    @Test
    public void testWithdraw() {
        WithdrawRequest withdraw = createWithdraw();
        service.withdraw(1L, withdraw);
        verify(transferService).transferCubeiaAccounting(1L, withdraw.getUserId(), withdraw.getFunds().getCurrency(), withdraw.getFunds().getAmount(), true);
    }
    
    @Test
    public void testDeposit() {
        DepositRequest deposit = createDeposit();
        service.deposit(1L, deposit);
        verify(transferService).transferCubeiaAccounting(1L, deposit.getUserId(), deposit.getFunds().getCurrency(), deposit.getFunds().getAmount(), false);
    }
    
    @Test
    public void testCreateAuditCalls() {
        // No audit on get balance calls
        when(audit.auditNewRequestLog(Mockito.anyLong(), Mockito.any(WalletDTO.class))).thenReturn(2L);
        BalanceRequest balance = createBalance();
        service.getBalance(1L, balance );
        verify(audit, Mockito.never()).auditNewRequestLog(Mockito.anyLong(), Mockito.any(BalanceRequest.class));
        verify(audit, Mockito.never()).auditCompleted(2L, null);
        
        when(audit.auditNewRequestLog(Mockito.anyLong(), Mockito.any(WalletDTO.class))).thenReturn(3L);
        WithdrawRequest withdraw = createWithdraw();
        service.withdraw(1L, withdraw);
        verify(audit, Mockito.times(1)).auditNewRequestLog(Mockito.anyLong(), Mockito.any(WithdrawRequest.class));
        verify(audit, Mockito.times(1)).auditCompleted(3L, null);
        
        when(audit.auditNewRequestLog(Mockito.anyLong(), Mockito.any(WalletDTO.class))).thenReturn(4L);
        DepositRequest deposit = createDeposit();
        service.deposit(1L, deposit);
        verify(audit, Mockito.times(2)).auditNewRequestLog(Mockito.anyLong(), Mockito.any(DepositRequest.class));
        verify(audit, Mockito.times(1)).auditCompleted(4L, null);
    }
    
    @Test
    public void testWithdrawDeniedByOperator() {
        WithdrawRequest withdraw = createWithdraw();
        when(client.withdraw(Mockito.any(WithdrawRequest.class))).thenThrow(new InsufficientFundsException("Test insufficient funds"));
        try {
            service.withdraw(12L, withdraw);
            fail("Should throw insufficient funds exception");
        } catch (InsufficientFundsException e) {}
        
        verify(audit, Mockito.times(1)).auditNewRequestLog(Mockito.eq(12L), Mockito.any(WithdrawRequest.class));
        verify(audit, Mockito.never()).auditCompleted(Mockito.anyLong(), Mockito.any(AbstractWalletResponse.class));        
        verify(audit, Mockito.times(1)).auditDenied(Mockito.anyLong());
    }
    
    @Test
    public void testBalanceRemoteFailure() {
        BalanceRequest request = createBalance();
        when(client.getBalance(Mockito.any(BalanceRequest.class))).thenThrow(new RuntimeException("Test remote fail"));
        try {
            service.getBalance(12L, request);
            fail("Should throw exception");
        } catch (RuntimeException e) {
            // Expected
        }
        
        verify(audit, Mockito.never()).auditNewRequestLog(Mockito.eq(12L), Mockito.any(DepositRequest.class));
        verify(audit, Mockito.never()).auditCompleted(Mockito.anyLong(), Mockito.any(AbstractWalletResponse.class));        
        verify(audit, Mockito.never()).auditDenied(Mockito.anyLong());
        verify(audit, Mockito.times(1)).auditBalanceRequestFailed(Mockito.anyLong(), Mockito.eq(request), Mockito.anyString());
    }
    
    @Test
    public void testWithdrawRemoteFailure() {
        WithdrawRequest request = createWithdraw();
        when(client.withdraw(Mockito.any(WithdrawRequest.class))).thenThrow(new RuntimeException("Test remote fail"));
        try {
            service.withdraw(12L, request);
            fail("Should throw exception");
        } catch (InsufficientFundsException e) {
            fail("Should throw runtime exception");
        } catch (RuntimeException e) {
            // Expected
        }
        
        verify(audit, Mockito.times(1)).auditNewRequestLog(Mockito.eq(12L), Mockito.any(WithdrawRequest.class));
        verify(audit, Mockito.never()).auditCompleted(Mockito.anyLong(), Mockito.any(AbstractWalletResponse.class));        
        verify(audit, Mockito.never()).auditDenied(Mockito.anyLong());
        verify(audit, Mockito.times(1)).auditFailed(Mockito.anyLong(), Mockito.anyString());
    }
    
    @Test
    public void testDepositRemoteFailure() {
        DepositRequest request = createDeposit();
        when(client.deposit(Mockito.any(DepositRequest.class))).thenThrow(new RuntimeException("Test remote fail"));
        try {
            service.deposit(12L, request);
            fail("Should throw exception");
        } catch (InsufficientFundsException e) {
            fail("Should throw runtime exception");
        } catch (RuntimeException e) {
            // Expected
        }
        
        verify(audit, Mockito.times(1)).auditNewRequestLog(Mockito.eq(12L), Mockito.any(DepositRequest.class));
        verify(audit, Mockito.never()).auditCompleted(Mockito.anyLong(), Mockito.any(AbstractWalletResponse.class));        
        verify(audit, Mockito.never()).auditDenied(Mockito.anyLong());
        verify(audit, Mockito.times(1)).auditFailed(Mockito.anyLong(), Mockito.anyString());
    }
    
    @Test
    public void testDepositMoreFundsThanBalance() {
        DepositRequest request = createDeposit();
        Mockito.doThrow(new NegativeBalanceException(1L)).when(transferService).transferCubeiaAccounting(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.any(BigDecimal.class), Mockito.anyBoolean());
        try {
            service.deposit(12L, request);
            fail("Should throw exception");
        } catch (NegativeBalanceException e) {
            // Expected
        }
        
        // audit log should be called with an internal fail status
        verify(audit).auditDeniedInternally(Mockito.anyLong(), Mockito.anyString());
        
        // Remote deposit should never be called since we have failed the internal accounting call
        verify(client, never()).deposit(Mockito.any(DepositRequest.class));
        verify(client, never()).withdraw(Mockito.any(WithdrawRequest.class));
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testFindEntries() {
        OperatorWalletRequestLog entry = new OperatorWalletRequestLog();
        entry.setId(100L);
        
        List<OperatorWalletRequestLog> log = new ArrayList<>();
        log.add(entry);
        when(operatorWalletDao.findLogEntries(Mockito.anyLong(), Mockito.any(Collection.class), Mockito.anyInt(), Mockito.anyInt(), Mockito.any(OperatorWalletRequestOrder.class), Mockito.anyBoolean())).thenReturn(log);
        when(operatorWalletDao.countLogEntries(Mockito.anyLong(), Mockito.any(Collection.class))).thenReturn(1);
        
        OperatorWalletRequestQueryResult queryResult = service.findWalletRequestLogEntries(1L, null , 0, 100, null, true);
        
        assertThat(queryResult, notNullValue());
        assertThat(queryResult.getTotalQueryResultSize(), is(1));
        assertThat(queryResult.getEntries().size(), is(1));
    }
    
    @Test
    public void testCopyBeanProperties() {
        OperatorWalletRequestLog entity = new OperatorWalletRequestLog();
        entity.setId(1L);
        entity.setMessage("Something");
        entity.setStatus(RequestStatus.DENIED);
        entity.setType("zoo");
        
        OperatorWalletRequestLogDTO dto = service.translate(entity);
        
        assertThat(dto.getId(), is(1L));
        assertThat(dto.getMessage(), is("Something"));
        assertThat(dto.getRequestStatus(), is(OperatorWalletRequestStatus.DENIED));
        assertThat(dto.getType(), is("zoo"));
        assertThat(dto.getTimestamp(), is(entity.getTimestamp()));
    }
    
    private DepositRequest createDeposit() {
        Money funds = new Money();
        funds.setAmount(new BigDecimal("10.0"));
        funds.setCurrency("EUR");
        
        DepositRequest request = new DepositRequest();
        request.setUserId(22L);
        request.setFunds(funds);
        request.setRequestId("XYZ");
        return request;
    }

    private WithdrawRequest createWithdraw() {
        Money funds = new Money();
        funds.setAmount(new BigDecimal("10.0"));
        funds.setCurrency("EUR");
        
        WithdrawRequest request = new WithdrawRequest();
        request.setUserId(22L);
        request.setFunds(funds);
        request.setRequestId("XYZ");
        return request;
    }

    private BalanceRequest createBalance() {
        BalanceRequest request = new BalanceRequest();
        request.setCurrency("EUR");
        request.setUserId(22L);
        request.setRequestId("ABC");
        return request;
    }

}

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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.wallet.exceptions.NotFoundException;
import com.cubeia.backoffice.wallet.api.dto.Account;
import com.cubeia.backoffice.wallet.api.dto.Currency;
import com.cubeia.backoffice.wallet.api.dto.report.TransactionEntry;
import com.cubeia.backoffice.wallet.api.dto.report.TransactionRequest;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;

public class TransferServiceTest {

    TransferService service;
    
    @Mock WalletServiceClient walletClient;
    
    @Mock Account playerAccount;
    
    @Mock Account operatorAccount;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new TransferService();
        service.walletClient = walletClient;
        
        when(walletClient.getOperatorAccount(1L, "EUR")).thenReturn(operatorAccount);
        when(walletClient.getAccount(22L, "EUR")).thenReturn(playerAccount);
        
        when(playerAccount.getId()).thenReturn(122L);
        when(operatorAccount.getId()).thenReturn(101L);
        
        when(walletClient.getCurrency("EUR")).thenReturn(new Currency("EUR", 2));
    }
    
    @Test
    public void testTransferWithdrawal() {
        Money money = new Money("EUR", "10");
        service.transferCubeiaAccounting(1L, 22L, money.getCurrency(), money.getAmount() , true);
        
        verify(walletClient).getOperatorAccount(1L, "EUR");
        
        ArgumentCaptor<TransactionRequest> argument = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(walletClient).doTransaction(argument.capture());
        
        TransactionRequest tx = argument.getValue();
        Collection<TransactionEntry> entries = tx.getEntries();

        assertThat(entries.size(), CoreMatchers.is(2));
        
        Iterator<TransactionEntry> iterator = entries.iterator();
        TransactionEntry playerEntry = iterator.next();
        TransactionEntry operatorEntry = iterator.next();
        
        assertThat(playerEntry.getAccountId(), is(122L));
        assertThat(playerEntry.getAmount().getAmount(), is(BD("10.00")));
        
        assertThat(operatorEntry.getAccountId(), CoreMatchers.is(101L));
        assertThat(operatorEntry.getAmount().getAmount(), is(BD("-10.00")));
    }

    @Test
    public void testTransferDeposit() {
        Money money = new Money("EUR", "20");
        service.transferCubeiaAccounting(1L, 22L, money.getCurrency(), money.getAmount() , false);
        
        verify(walletClient).getOperatorAccount(1L, "EUR");
        
        ArgumentCaptor<TransactionRequest> argument = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(walletClient).doTransaction(argument.capture());
        
        TransactionRequest tx = argument.getValue();
        Collection<TransactionEntry> entries = tx.getEntries();

        assertThat(entries.size(), CoreMatchers.is(2));
        
        Iterator<TransactionEntry> iterator = entries.iterator();
        TransactionEntry playerEntry = iterator.next();
        TransactionEntry operatorEntry = iterator.next();
        
        assertThat(playerEntry.getAccountId(), is(122L));
        assertThat(playerEntry.getAmount().getAmount(), is(BD("-20.00")));
        
        assertThat(operatorEntry.getAccountId(), CoreMatchers.is(101L));
        assertThat(operatorEntry.getAmount().getAmount(), is(BD("20.00")));
    }

    @Test(expected=NotFoundException.class)
    public void testAccountNotFound() {
        Money money = new Money("EUR", "20");
        service.transferCubeiaAccounting(1L, 224L, money.getCurrency(), money.getAmount() , false);
        
    }
    
    private BigDecimal BD(String value) {
        return new BigDecimal(value);
    }
}

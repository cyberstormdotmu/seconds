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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubeia.backoffice.accounting.api.Money;
import com.cubeia.backoffice.operator.service.wallet.WalletClientFactory;
import com.cubeia.backoffice.operator.wallet.exceptions.NotFoundException;
import com.cubeia.backoffice.wallet.api.dto.Account;
import com.cubeia.backoffice.wallet.api.dto.Currency;
import com.cubeia.backoffice.wallet.api.dto.report.TransactionEntry;
import com.cubeia.backoffice.wallet.api.dto.report.TransactionRequest;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class TransferService {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired WalletClientFactory walletClientFactory;
    
    WalletServiceClient walletClient;
    
    
    /**
     * Caches currency definitions as by the Wallet
     */
    private LoadingCache<String, Currency> currencies = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<String, Currency>() {
                 public Currency load(String currencyCode) {
                     return loadCurrency(currencyCode);
                   }
                 });
    
    
    @PostConstruct
    public void init() {
        walletClient = walletClientFactory.getWalletClient();
    }
    
    public void transferCubeiaAccounting(Long operatorId, Long playerId, String currencyCode, BigDecimal amount, boolean transferToPlayerAccount) {
        log.debug("Transfer from op2player: PID["+playerId+"] Currency["+currencyCode+"] Amount["+amount+"]");
        
        try {
            Currency currency = currencies.get(currencyCode);
        
            log.debug("Wallet client: "+walletClient);
            
            Account playerAccount = walletClient.getAccount(playerId, currencyCode);
            log.debug("Transfer Funds. Player Account: "+playerAccount);
            
            Account operatorAccount = walletClient.getOperatorAccount(operatorId, currencyCode);
            log.debug("Transfer Funds. Operator Account: "+playerAccount);
            
            if (playerAccount == null) {
                log.error("Player account not found. Player["+playerId+"] currency["+currencyCode+"]. " +
                        "Funds have been withdrawn from Operator["+operatorId+"] but internal transfer will fail. Manual correction needed.");
                throw new NotFoundException("Player account not found. Player["+playerId+"] currency["+currencyCode+"]");
            }
            
            if (operatorAccount == null) {
                log.error("Operator account not found. OperatorId["+operatorId+"] currency["+currencyCode+"]. " +
                        "Funds have been withdrawn from Operator["+operatorId+"] and Player["+playerId+"] but internal transfer will fail. Manual correction needed.");
                throw new NotFoundException("Operator account not found. OperatorId["+operatorId+"] currency["+currencyCode+"]");
            }
            
            // Default deposit to operator
            BigDecimal playerAmount = amount.negate();
            BigDecimal operatorAmount = amount;
            
            if (transferToPlayerAccount) {
                playerAmount = amount;
                operatorAmount = amount.negate();
            }
            
            TransactionEntry playerEntry = new TransactionEntry();
            playerEntry.setAccountId(playerAccount.getId());
            playerEntry.setAmount(new Money(currency.getCode(), currency.getFractionalDigits(), playerAmount));
            
            TransactionEntry operatorEntry = new TransactionEntry();
            operatorEntry.setAccountId(operatorAccount.getId());
            operatorEntry.setAmount(new Money(currency.getCode(), currency.getFractionalDigits(), operatorAmount));
            
            // Using LinkedHashSet to retain order which is used in unit-tests to verify entries.
            // If you change the order below then some unit tests will break.
            Collection<TransactionEntry> entries = new LinkedHashSet<>();
            entries.add(playerEntry);
            entries.add(operatorEntry);
            
            TransactionRequest transaction = new TransactionRequest();
            transaction.setComment("");
            transaction.setEntries(entries);
            walletClient.doTransaction(transaction);
            
        } catch (ExecutionException e) {
            log.error("Failed to execute player transfer with Cubeia Network. operatorId["+operatorId+"] playerId["+playerId+"] currencyCode["+currencyCode+"] amount["+amount+"] transferToPlayerAccount["+transferToPlayerAccount+"]", e);
            throw new RuntimeException("Failed to execute player transfer with Cubeia Network. operatorId["+operatorId+"] playerId["+playerId+"] currencyCode["+currencyCode+"] amount["+amount+"] transferToPlayerAccount["+transferToPlayerAccount+"]", e);
        }
    }
    
    
    protected Currency loadCurrency(String currencyCode) {
        return walletClient.getCurrency(currencyCode);
    }
}

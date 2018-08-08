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
package com.cubeia.network.web.wallet;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.wallet.api.dto.AccountQueryResult;
import com.cubeia.backoffice.wallet.api.dto.TransactionQueryResult;
import com.cubeia.backoffice.wallet.api.dto.request.ListAccountsRequest;
import com.cubeia.backoffice.wallet.api.dto.request.ListTransactionsRequest;
import com.cubeia.backoffice.wallet.client.WalletServiceClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component("walletCache")
public class WalletCache {
    private Logger log = LoggerFactory.getLogger(getClass());

    private static final int CACHE_SIZE = 10000;
    private static final int CACHE_TTL_MINUTES = 2;

    @Inject
    private WalletServiceClient walletService;
    
    private LoadingCache<ListAccountsRequest, AccountQueryResult> accountCache = CacheBuilder.newBuilder()
        .maximumSize(CACHE_SIZE)
        .expireAfterWrite(CACHE_TTL_MINUTES, TimeUnit.MINUTES)
        .build(new AccountsCacheLoader());

    private LoadingCache<ListTransactionsRequest, TransactionQueryResult> txCache = CacheBuilder.newBuilder()
        .maximumSize(CACHE_SIZE)
        .expireAfterWrite(CACHE_TTL_MINUTES, TimeUnit.MINUTES)
        .build(new TxCacheLoader());
    
    @SuppressWarnings("serial")
    private final class AccountsCacheLoader extends CacheLoader<ListAccountsRequest, AccountQueryResult> implements Serializable {
        public AccountQueryResult load(ListAccountsRequest req) throws Exception {
            log.debug("getting accounts from wallet service: {}", req);
            return walletService.listAccounts(req);
        }
    }
    
    @SuppressWarnings("serial")
    private final class TxCacheLoader extends CacheLoader<ListTransactionsRequest, TransactionQueryResult> implements Serializable {
        public TransactionQueryResult load(ListTransactionsRequest req) throws Exception {
            log.debug("getting transactions from wallet service: {}", req);
            return walletService.listTransactions(req);
        }
    }
    
    public LoadingCache<ListAccountsRequest, AccountQueryResult> getAccountCache() {
        return accountCache;
    }
    
    public LoadingCache<ListTransactionsRequest, TransactionQueryResult> getTxCache() {
        return txCache;
    }

}

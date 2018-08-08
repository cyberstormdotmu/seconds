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

package com.cubeia.backoffice.wallet;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cubeia.backoffice.accounting.core.domain.AccountsOrder;
import com.cubeia.backoffice.accounting.core.domain.QueryResultsContainer;
import com.cubeia.backoffice.accounting.core.domain.TransactionsOrder;
import com.cubeia.backoffice.accounting.core.entity.Account;
import com.cubeia.backoffice.accounting.core.entity.Transaction;
import com.cubeia.backoffice.accounting.core.integrations.PluginManager;
import com.cubeia.backoffice.accounting.core.util.DTOFactory;
import com.cubeia.backoffice.wallet.manager.WalletAccountingManager;

@Component("wallet.service.indexingService")
public class IndexerServiceImpl implements IndexerService {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource(name = "wallet.service.accountingManager")
    protected WalletAccountingManager accountingManager;

    @Resource(name = "accounting.DTOFactory")
    protected DTOFactory dtoFactory;

    @Resource(name = "accounting.pluginManager")
    private PluginManager pluginManager;
    
    @Resource
    private PlatformTransactionManager txManager;

    protected abstract class BatchWorker {
        private int offset = 0;
        private int batchSize = 1000;
        private int totalSize = -1;
        private boolean done = false;
        private boolean shouldGoOn = true;
        private Thread thread;
        private String name;
        
        public BatchWorker(String name) {
            this.name = name;
            start();
        }
        
        private void start() {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (shouldGoOn  &&  !done) {
                            totalSize = getTransactionTemplate().execute(new TransactionCallback<Integer>() {
                                @Override
                                public Integer doInTransaction(TransactionStatus status) {
                                    log.debug("{}: {}", name, getStatsString());
                                    return doStuff(offset, batchSize);
                                }
                            });
                            offset += batchSize;
                            done = offset >= totalSize;
                        }
                    } catch (Exception e) {
                        log.error("error indexing", e);
                    } finally {
                        done = true;
                    }
                }
            });
            thread.start();
        }
        
        protected abstract int doStuff(int offset, int batchSize);
        
        public int getTotalSize() { return totalSize; }
        
        public int getOffset() {
            return offset;
        }
        
        public boolean isDone() { return done; }
        
        public boolean shouldGoOn() { return shouldGoOn; }
        
        public void stop() { shouldGoOn = false; }
        
        public String toString() {
            return (done ? "DONE" : "RUNNING: " + getStatsString());
        }
        
        public String getStatsString() {
            String percentage = totalSize == -1 ? "unknown" : "" + (100f * offset / totalSize) + "%";
            String tot = totalSize == -1 ? "unknown" : "" + totalSize;
            return "" + getOffset() + " - " + (getOffset() + batchSize) + " / " + tot + " (" + percentage + ")"; 
        }
    };
    
    
    private BatchWorker accountWorker;    
    private BatchWorker txWorker;    
    
    private TransactionTemplate txTemplate;
    
    private TransactionTemplate getTransactionTemplate() {
        if (txTemplate == null) {
            txTemplate = new TransactionTemplate(txManager);
            txTemplate.setReadOnly(true);
        }
        return txTemplate;
    }
    
    
    
    /* (non-Javadoc)
     * @see com.cubeia.backoffice.wallet.IndexerService#reindexAccount(java.lang.Long)
     */
    @Override
    public String reindexAccount(Long accountId) {
        log.debug("reindexing account: {}", accountId);
        
        Account account = accountingManager.getAccount(accountId);
        if (account != null) {
            pluginManager.afterUpdate(dtoFactory.toDTO(account));
        }
        
        return "OK";
    }
    
    /* (non-Javadoc)
     * @see com.cubeia.backoffice.wallet.IndexerService#reindexAllAccounts()
     */
    @Override
    public synchronized String reindexAllAccounts() {
        
        if (accountWorker == null  ||  accountWorker.isDone()) {
            log.debug("reindexing all users");
            accountWorker = new BatchWorker("reindex accounts") {
                @Override
                protected int doStuff(int offset, int batchSize) {
                    QueryResultsContainer<Account> accountsResult = accountingManager.listAccounts(null, null, null, null, null, null, offset, batchSize, AccountsOrder.ID, true);
                    for (Account account : accountsResult.getResults()) {
                        pluginManager.afterUpdate(dtoFactory.toDTO(account));
                    }
                    return accountsResult.getTotalQueryResultSize();
                }
            };
            return "OK";
        } else {
            log.debug("indexing accounts already in progress, won't spawn new thread");
            return accountWorker.toString();
        }
    }
    
    
    
    
    @Override
    public synchronized String reindexAllAccountsStatus() {
        if (accountWorker == null  ||  accountWorker.isDone()) {
            return "NOT RUNNING";
        } else {
            return accountWorker.toString();
        }
    }
    
    @Override
    public synchronized String stopReindexAllAccounts() {
        if (accountWorker != null) {
            log.debug("stopping reindexing of all accounts");
            accountWorker.stop();
            return "STOPPED";
        } else {
            return "NOT RUNNING";
        }
    }
    
    

    /* (non-Javadoc)
     * @see com.cubeia.backoffice.wallet.IndexerService#reindexTransaction(java.lang.Long)
     */
    @Override
    public String reindexTransaction(Long txId) {
        log.debug("reindexing transaction: {}", txId);
        
        Transaction tx= accountingManager.getTransactionById(txId);
        if (tx != null) {
            pluginManager.afterUpdate(dtoFactory.toDTO(tx));
        }
        
        return "OK";
    }
    
    /* (non-Javadoc)
     * @see com.cubeia.backoffice.wallet.IndexerService#reindexTransactions()
     */
    @Override
    public synchronized String reindexAllTransactions() {
        if (txWorker == null  ||  txWorker.isDone()) {
            log.debug("reindexing all transactions");
            txWorker = new BatchWorker("reindex transactions") {
                @Override
                protected int doStuff(int offset, int batchSize) {
                    QueryResultsContainer<Transaction> txResult = accountingManager.listTransactions(null, null, null, null, offset, batchSize, TransactionsOrder.ID, true);
                    for (Transaction tx : txResult.getResults()) {
                        pluginManager.afterUpdate(dtoFactory.toDTO(tx));
                    }
                    
                    return txResult.getTotalQueryResultSize();
                }
            };
            return "OK";
        } else {
            log.debug("indexing transactions already in progress, won't spawn new thread");
            return txWorker.toString();
        }
    }
    
    
    @Override
    public synchronized String reindexAllTransactionsStatus() {
        if (txWorker == null  ||  txWorker.isDone()) {
            return "NOT RUNNING";
        } else {
            return txWorker.toString();
        }
    }
    
    @Override
    public synchronized String stopReindexAllTransactions() {
        if (txWorker != null) {
            log.debug("stopping reindexing of all transactions");
            txWorker.stop();
            return "STOPPED";
        } else {
            return "NOT RUNNING";
        }
    }
    
}

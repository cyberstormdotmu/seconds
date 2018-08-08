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

package com.cubeia.backoffice.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.cubeia.backoffice.users.api.UserOrder;
import com.cubeia.backoffice.users.domain.UserQueryResultContainer;
import com.cubeia.backoffice.users.entity.User;
import com.cubeia.backoffice.users.integrations.PluginFactory;
import com.cubeia.backoffice.users.manager.UserManager;
import com.cubeia.backoffice.users.util.DTOFactory;

@Component("user.service.indexingService")
public class IndexerServiceImpl implements IndexerService {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserManager userManager;

    @Autowired
    private PluginFactory pluginFactory; 
    
    @Autowired
    private PlatformTransactionManager txManager;
    
    protected DTOFactory dtoFactory = new DTOFactory();

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
                        //log.debug("done indexing {} users", totalSize);
                    } catch (Exception e) {
                        //log.error("error indexing users", e);
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
    
    private BatchWorker worker;

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
    public String reindexUser(Long userId) {
        log.debug("reindexing user: {}", userId);
        
        User user = userManager.getUserById(userId);
        if (user != null) {
            pluginFactory.afterUpdate(dtoFactory.createUserDTOByEntity(user));
        }
        
        return "OK";
    }

    @Override
    public synchronized String reindexAllUsers() {
        log.debug("reindexing all users");
        
        if (worker == null  ||  worker.isDone()) {
            worker = new BatchWorker("reindex users") {
                @Override
                protected int doStuff(int offset, int batchSize) {
                    UserQueryResultContainer usersResult;
                    usersResult = userManager.findUsers(null, null, null, offset, batchSize, UserOrder.ID, true);
                    for (User user : usersResult.getUsers()) {
                        pluginFactory.afterUpdate(dtoFactory.createUserDTOByEntity(user));
                    }
                    return usersResult.getTotalQueryResultSize();
                }
            };
            return "OK";
        } else {
            return worker.toString();
        }
    }

    @Override
    public synchronized String reindexAllUsersJobStatus() {
        if (worker == null  ||  worker.isDone()) {
            return "NOT RUNNING";
        } else {
            return worker.toString();
        }
    }
    
    @Override
    public synchronized String stopReindexAllUsers() {
        if (worker != null) {
            log.debug("stopping reindexing of all users");
            worker.stop();
            return "STOPPED";
        } else {
            return "NOT RUNNING";
        }
    }
    
    
}

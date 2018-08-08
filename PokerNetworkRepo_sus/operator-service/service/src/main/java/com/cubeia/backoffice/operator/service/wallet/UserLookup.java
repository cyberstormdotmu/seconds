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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.users.api.dto.User;
import com.cubeia.backoffice.users.client.UserServiceClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class UserLookup {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired UserServiceClient userClient;
    
    /**
     * Caches user to external user id
     */
    private LoadingCache<Long, User> users = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(10000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, User>() {
                public User load(Long userId) {
                    return loadExternalUserId(userId);
                }
            });

    protected User loadExternalUserId(Long userId) {
        return userClient.getUserById(userId);
    }
    
    public String getExternalId(long userId) {
        try {
            User user = users.get(userId);
            log.debug("Loaded user["+userId+"]: "+user);
            return user.getExternalUserId();
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("User external id could not be loaded for user id["+userId+"]", e);
        }
    }
    
}

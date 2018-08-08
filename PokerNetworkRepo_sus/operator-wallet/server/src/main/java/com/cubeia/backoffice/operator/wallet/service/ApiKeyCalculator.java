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
package com.cubeia.backoffice.operator.wallet.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.operator.wallet.config.OperatorApiKey;
import com.cubeia.backoffice.operator.wallet.web.ApiException;

@Component
public class ApiKeyCalculator {

    @Autowired OperatorApiKey apiKey;

    Logger log = LoggerFactory.getLogger(getClass());
    
    public void verifyApiKey(String hashedKey, String requestId) {
        try {
            String field = apiKey.get() + requestId;
            byte[] bytes = field.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdbytes = md.digest(bytes);
            
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
              sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            String md5signature = sb.toString();
            
            if (hashedKey != null && hashedKey.equalsIgnoreCase(md5signature)) {
                log.debug("HASH KEY SIGNATURE MATCHES");
            } else {
                log.debug("HASH KEY SIGNATURE MATCH FAIL!!!");
                throw new ApiException(Status.UNAUTHORIZED);
            }
            
        } catch (NoSuchAlgorithmException e) {
           throw new RuntimeException("Failed to create MD5 hash", e);
        }
        
    }
    
}

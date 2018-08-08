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

import static org.mockito.Mockito.when;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cubeia.backoffice.operator.wallet.config.OperatorApiKey;
import com.cubeia.backoffice.operator.wallet.web.ApiException;

public class ApiKeyCalculatorTest {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    ApiKeyCalculator calc = new ApiKeyCalculator(); 
    
    @Mock OperatorApiKey apiKey;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        calc.apiKey = apiKey;
        when(apiKey.get()).thenReturn("ABC");
    }
    
    @Test
    public void testApiKeyHash() throws Exception {
        String apiKey = "ABC";
        String requestId = UUID.randomUUID().toString();
        String md5 = createMD5(apiKey, requestId);
        calc.verifyApiKey(md5, requestId);
    }
    
    @Test(expected=ApiException.class)
    public void testApiKeyHashBad() throws Exception {
        String apiKey = "FAIL";
        String requestId = UUID.randomUUID().toString();
        String md5 = createMD5(apiKey, requestId);
        calc.verifyApiKey(md5, requestId);
    }

    private String createMD5(String apiKey, String requestId) throws NoSuchAlgorithmException {
        String field = apiKey + requestId;
        byte[] bytes = field.getBytes();
        
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdbytes = md.digest(bytes);
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}

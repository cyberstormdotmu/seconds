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
package com.cubeia.backoffice.operator.wallet.config;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * The following runtime configuration can be overridden with System Properties:
 * 
 * verifyApiKey = true:
 * -Dapikey.verify
 * 
 * verifyApiKey = false:
 * -Dapikey.ignore
 * 
 * 
 * @author Fredrik
 *
 */
@Service
public class OperatorWalletRuntimeConfig implements OperatorWalletRuntimeConfigMBean {
    
    Logger log = LoggerFactory.getLogger(this.getClass());
    
    private boolean verifyApiKey = true;
    
    private boolean throwError = false;
    
    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
            ObjectName name = new ObjectName("com.cubeia.operatorwallet:type=Config"); 
            mbs.registerMBean(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    @Override
    public boolean isVerifyApiKey() {
        if (System.getProperty("apikey.verify") != null) {
            return true;
        } else if (System.getProperty("apikey.ignore") != null) {
            return false;
        } else {
            return verifyApiKey;
        }
    }

    @Override
    public void setVerifyApiKey(boolean value) {
        verifyApiKey = value;
    }

    public boolean isThrowError() {
        return throwError;
    }
    
    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }
}

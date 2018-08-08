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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Provides a configurable way of setting and getting the current Operator API Key.
 * 
 * The following configurations are available and are prioritized in this order:
 * 
 * 1. User system-property operator.key. Example:
 *    -Doperator.key=550e8400-e29b-41d4-a716-446655440000
 *    
 *    
 * 2. Create a propery file in classpath called operator-key.properties which has
 *    one key defined, operator.key. Example:
 *    operator.key=550e8400-e29b-41d4-a716-446655440000
 *    
 *    
 * 3. If nothing is specified then service will fall back to the default id of:
 *    82813920-a9d2-11e3-a5e2-0800200c9a66
 * 
 * 
 * 
 * 
 * @author Fredrik
 *
 */
@Component
public class OperatorApiKey {
	
	private static String DEFAULT_PROPERTY_FILE = "operator-key.properties";
	
	protected static String DEFAULT_API_KEY = "82813920-a9d2-11e3-a5e2-0800200c9a66";
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	private Properties props;
	
	private String propertyFile = DEFAULT_PROPERTY_FILE;
	
	private String apiKey = DEFAULT_API_KEY;
	
	public OperatorApiKey() {
		this(DEFAULT_PROPERTY_FILE);
	}
	
	public OperatorApiKey(String customPropertyFile) {
	    this.propertyFile = customPropertyFile;
        setApiKey();
        log.info("Operator wallet is configured with the following Operator API Key: "+apiKey);
    }
	
	private void setApiKey() {
	    apiKey = System.getProperty("operator.key");
        if (apiKey == null || apiKey == "") {
            apiKey = DEFAULT_API_KEY;
            load();
        }
	}
	
	private void load() {
	    try {
            props = new Properties();
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
            
            if (inputStream != null) {
    			props.load(inputStream);
    			if (props.containsKey("operator.key")) {
    			    apiKey = props.getProperty("operator.key");
    			}
            }
	    } catch (IOException e) {
            log.warn("Failed to load operator API key property file.", e);
        }
	}
	
	public String get() {
	    return apiKey;
	}
}

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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OperatorApiKeyTest {

    OperatorApiKey key;
    
    @Before
    public void setup() {
        System.clearProperty("operator.key");
    }
    
    @Test
    public void testDefaultKey() {
        key = new OperatorApiKey();
        Assert.assertThat(key.get(), CoreMatchers.is(OperatorApiKey.DEFAULT_API_KEY));
    }
    
    
    @Test
    public void testSystemKey() {
        System.setProperty("operator.key", "ABC");
        key = new OperatorApiKey();
        Assert.assertThat(key.get(), CoreMatchers.is("ABC"));
        
    }
    
    @Test
    public void testPropertyFile() {
        key = new OperatorApiKey("test-operator-key.properties");
        Assert.assertThat(key.get(), CoreMatchers.is("XYZ"));
    }
    
    @Test
    public void testMissingPropertyFile() {
        key = new OperatorApiKey("XXX-operator-key.properties");
        Assert.assertThat(key.get(), CoreMatchers.is(OperatorApiKey.DEFAULT_API_KEY));
    }
    
    @Test
    public void testSystemPropertyOverridesPropertyFile() {
        System.setProperty("operator.key", "ABC");
        key = new OperatorApiKey("test-operator-key.properties");
        Assert.assertThat(key.get(), CoreMatchers.is("ABC"));
    }

}

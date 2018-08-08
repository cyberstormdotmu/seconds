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

import static com.cubeia.backoffice.operator.service.entity.OperatorConfigParameter.CURRENCIES;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cubeia.backoffice.operator.api.OperatorCurrencyDTO;
import com.cubeia.backoffice.operator.service.manager.OperatorManager;

public class OperatorServiceTest {

    
    @Mock
    private OperatorManager operatorManager;
    
    private OperatorServiceImpl operatorService;
    
    
    @Before
    public void setup() {
        initMocks(this);
        
        operatorService = new OperatorServiceImpl();
        operatorService.operatorManager = operatorManager;
    }
    
    @Test
    public void testGetOperatorCurrencies() {
        Long opId = 123L;
        
        String json = "[{\"code\" : \"XOC\", \"name\" : \"QPC\", \"longName\" : \"QPC\", \"symbol\" : \"$\"}, "
            + "{\"code\" : \"XCC\", \"name\" : \"XCC\", \"longName\" : \"XCC\", \"symbol\" : \"XCC\"}]";
        
        when(operatorManager.getConfig(opId, CURRENCIES)).thenReturn(json);
        
        List<OperatorCurrencyDTO> operatorCurrencies = operatorService.getOperatorCurrencies(opId);
        assertThat(operatorCurrencies.size(), is(2));
        assertThat(operatorCurrencies.get(0), is(new OperatorCurrencyDTO("XOC", "QPC", "QPC", "$")));
        assertThat(operatorCurrencies.get(1), is(new OperatorCurrencyDTO("XCC", "XCC", "XCC", "XCC")));
    }
    
    @Test
    public void testGetOperatorCurrenciesBadJsonNoList() {
        Long opId = 123L;
        String json = "{\"code\" : \"XOC\", \"name\" : \"QPC\", \"longName\" : \"QPC\", \"symbol\" : \"$\"}";
        
        when(operatorManager.getConfig(opId, CURRENCIES)).thenReturn(json);
        
        List<OperatorCurrencyDTO> operatorCurrencies = operatorService.getOperatorCurrencies(opId);
        assertThat(operatorCurrencies.isEmpty(), is(true));
    }
    
    @Test
    public void testGetOperatorCurrenciesBadJson() {
        Long opId = 123L;
        String json = "[{\"knark\" : \"XOC\"}]";
        
        when(operatorManager.getConfig(opId, CURRENCIES)).thenReturn(json);
        
        List<OperatorCurrencyDTO> operatorCurrencies = operatorService.getOperatorCurrencies(opId);
        assertThat(operatorCurrencies.isEmpty(), is(true));
    }
    
    @Test
    public void testGetOperatorCurrenciesEmpty() {
        Long opId = 123L;
        String json = "";
        
        when(operatorManager.getConfig(opId, CURRENCIES)).thenReturn(json);
        
        List<OperatorCurrencyDTO> operatorCurrencies = operatorService.getOperatorCurrencies(opId);
        assertThat(operatorCurrencies.isEmpty(), is(true));
    }
}

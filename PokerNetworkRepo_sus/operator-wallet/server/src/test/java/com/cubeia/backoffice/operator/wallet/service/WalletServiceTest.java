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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.ws.rs.core.Response.Status;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.cubeia.backoffice.operator.api.Money;
import com.cubeia.backoffice.operator.wallet.web.ApiException;

public class WalletServiceTest {

	WalletService service;
	
	@Before
	public void setup() {
		service = new WalletService();
	}
	
	
	@Test
	public void testCreateBalance() {
	    Money balance = service.getBalance(22L, "EUR");
		assertThat(balance, CoreMatchers.notNullValue());
		assertThat(balance.getAmount(), CoreMatchers.equalTo(new BigDecimal("1000.00")));
		assertThat(service.currencies.size(), CoreMatchers.is(1));
		
		Money balance2 = service.getBalance(22L, "EUR");
		assertThat(balance2, CoreMatchers.notNullValue());
		assertThat(balance2.getAmount(), CoreMatchers.equalTo(new BigDecimal("1000.00")));
		assertThat(service.currencies.size(), CoreMatchers.is(1));
		
		Money balance3 = service.getBalance(22L, "USD");
		assertThat(balance3, CoreMatchers.notNullValue());
		assertThat(balance3.getAmount(), CoreMatchers.equalTo(new BigDecimal("1000.00")));
		assertThat(service.currencies.size(), CoreMatchers.is(2));
	}
	
	@Test(expected=ApiException.class)
	public void testGetBalanceNullUser() {
		service.getBalance(null, "EUR");
		assertThat(service.currencies.size(), CoreMatchers.is(0));
	}
	
	@Test
	public void testWithdraw() {
	    service.withdraw(1L, "EUR", new BigDecimal("100.00"));
	    Money balance = service.getBalance(1L, "EUR");
	    assertThat(balance.getAmount(), equalTo(new BigDecimal("900.00")));
	}
	
	@Test
    public void testWithdrawTooMuch() {
        try {
            service.withdraw(2L, "EUR", new BigDecimal("1100.00"));
            Assert.fail("Exception expected");
        } catch (ApiException e) {
            assertThat(e.getResponse().getStatus(), CoreMatchers.is(402));
        }
    }
	
	@Test
    public void testDeposit() {
        service.deposit(1L, "EUR", new BigDecimal("100.00"));
        Money balance = service.getBalance(1L, "EUR");
        assertThat(balance.getAmount(), equalTo(new BigDecimal("1100.00")));
    }
	
	@Test
    public void testNegativeDeposit() {
	    try {
            service.deposit(2L, "EUR", new BigDecimal("-10"));
            Assert.fail("Exception expected");
        } catch (ApiException e) {
            assertThat(e.getResponse().getStatus(), CoreMatchers.is(Status.BAD_REQUEST.getStatusCode()));
        }
    }
	
}

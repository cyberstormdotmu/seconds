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

package com.cubeia.backoffice.wallet.service.http;

import com.cubeia.backoffice.wallet.WalletService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.WebApplicationException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class CurrencyResourceTest {

	@Mock
	private WalletService walletService;

	@InjectMocks
	private CurrencyResource resource;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCurrencyShouldResultIn404IfCurrencyNotFound() throws Exception {
		when(walletService.getSupportedCurrency("EUR")).thenReturn(null);

		try {
			resource.getCurrency("EUR");
		} catch (WebApplicationException e) {
			assertThat(e.getResponse().getStatus(), is(404));
		}
	}
}

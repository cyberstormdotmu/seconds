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

package com.cubeia.backoffice.wallet.client;

import com.cubeia.backoffice.wallet.api.dto.AccountBalanceResult;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WalletServiceClientHTTPTest {

	private WalletServiceClientHTTP client;

	private static final Logger log = Logger.getLogger(WalletServiceClientHTTPTest.class);

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(9393);

	@Before
	public void setUp() {
		client = new WalletServiceClientHTTP("http://localhost:9393/wallet-service");
	}

	@Test
	public void testGetAccountBalancesByUser() throws Exception {
		stubFor(get(urlMatching("/somewhere")).willReturn(aResponse().withStatus(200).withBody("some response")));
		stubFor(get(urlEqualTo("/wallet-service/wallet/balances/1"))
				.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody("[ { \"accountId\" : 1,\n" +
						"    \"balance\" : { \"amount\" : 1000.0,\n" +
						"        \"currency\" : \"XCC\",\n" +
						"        \"fractionalDigits\" : 2\n" +
						"      }\n" +
						"  },\n" +
						"  { \"accountId\" : 2,\n" +
						"    \"balance\" : { \"amount\" : 0.25,\n" +
						"        \"currency\" : \"XOC\",\n" +
						"        \"fractionalDigits\" : 2\n" +
						"      }\n" +
						"  }\n" +
						"]")));

		List<AccountBalanceResult> balances = client.getAccountBalancesByUser(1L);
		assertThat(balances.get(0).getBalance().getAmount().toBigInteger().intValue(), is(1000));
	}

	@Test
	public void testGetAccountBalancesByUserNotFound() throws Exception {
		stubFor(get(urlEqualTo("/wallet-service/wallet/balances/1")).willReturn(aResponse().withStatus(404)));

		List<AccountBalanceResult> balances = client.getAccountBalancesByUser(1L);
		assertThat(balances.size(), is(0));
	}

}

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
package com.cubeia.network.web.search;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.IOException;

import com.cubeia.network.web.search.Transaction;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.cubeia.network.web.search.Transaction.Entry;

public class TransactionTest {

	@Test
	public void testFromJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Transaction tx = mapper.readValue(getClass().getResourceAsStream("/json/search/tx1.json"), Transaction.class);
		
		assertThat(tx.getTransactionId(), is(15L));
		assertThat(tx.getTimestamp().getTime(), is(1379331521215L));
		assertThat(tx.getExternalId(), is("tx123"));
		
		assertThat(tx.getAttributes().get("adminUser").getValue(), is("admin"));
		assertThat(tx.getAttributes().get("adminBrowser").getValue(), containsString("Mozilla/5.0"));
		assertThat(tx.getAttributes().get("adminIp").getValue(), is("127.0.0.1"));
		
		Entry entry0 = tx.getEntries().get(0);
		assertThat(entry0.getEntryId(), is(27L));
		assertThat(entry0.getAmount(), is(-123456L));
		assertThat(entry0.getAccount().getAccountId(), is(1L));
		assertThat(entry0.getAccount().getCurrencyCode(), is("EUR"));
		assertThat(entry0.getAccount().getUserId(), is(1L));
		
		Entry entry1 = tx.getEntries().get(1);
		assertThat(entry1.getEntryId(), is(28L));
		assertThat(entry1.getAmount(), is(123456L));
		assertThat(entry1.getAccount().getAccountId(), is(3L));
		assertThat(entry1.getAccount().getCurrencyCode(), is("EUR"));
		assertThat(entry1.getAccount().getUserId(), is(200L));
	}

}

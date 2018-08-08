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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import com.cubeia.network.web.search.Account;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

public class AccountTest {

	@Test
	public void testFromJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Account account = mapper.readValue(getClass().getResourceAsStream("/json/search/account1.json"), Account.class);
		
		assertThat(account.getAccountId(), is(5L));
		assertThat(account.getUserId(), is(50L));
		assertThat(account.getClosed(), nullValue());
		assertThat(account.getCreated().getTime(), is(1379335535922L));
		assertThat(account.getCurrencyCode(), is("EUR"));
		assertThat(account.getFractionalDigits(), is(2));
		assertThat(account.getName(), is("test"));
		assertThat(account.getStatus(), is("OPEN"));
		assertThat(account.getType(), is("STATIC_ACCOUNT"));
		assertThat(account.getUserId(), is(50L));
		assertThat(account.getWalletId(), nullValue());
		assertThat(account.getAttributes().get("objectId").getValue(), is("obj"));
		assertThat(account.getAttributes().get("gameId").getValue(), is("g123"));
		assertThat(account.getAttributes().get("gameName").getValue(), is("test game"));
		
	}

}

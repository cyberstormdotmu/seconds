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

import java.io.IOException;

import com.cubeia.network.web.search.User;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class UserTest {
	
	@Test
	public void testFromJson() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		User user = mapper.readValue(getClass().getResourceAsStream("/json/search/user1.json"), User.class);
		
		assertThat(user.getUserId(), CoreMatchers.is(1L));
		assertThat(user.getExternalUserId(), CoreMatchers.is("abc123"));
		assertThat(user.getUserName(), CoreMatchers.is("smaxxor1"));
		assertThat(user.getOperatorId(), CoreMatchers.is(10L));
		assertThat(user.getStatus(), CoreMatchers.is("ENABLED"));
		assertThat(user.getUserType(), CoreMatchers.is("USER"));
		assertThat(user.getUserInformation().getFirstName(), CoreMatchers.is("Tobias"));
		assertThat(user.getUserInformation().getLastName(), CoreMatchers.is("Testerblom"));
		assertThat(user.getUserInformation().getEmail(), CoreMatchers.is("a@example.com"));
		assertThat(user.getAttributes().get("attr1").getValue(), is("value1"));
		assertThat(user.getAttributes().get("attr2").getValue(), is("value2"));
	}

}

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

package com.cubeia.firebase.bot;

import java.util.Map;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.defined.Parameter;
import com.cubeia.firebase.api.util.ParameterUtil;
import com.cubeia.firebase.bot.model.Table;
import com.cubeia.firebase.io.protocol.Param;

public class DefaultBotGroupConfig implements BotGroupConfig {

	private transient Logger log = Logger.getLogger(this.getClass());
	 
	private String currency;
	
	private Map<String, String> lobbyAttributes;
	
	@Override
	public String createBotScreenName(int id) {
		return "Bot_" + id;
	}

	@Override
	public String createLobbyBotScreenName(int id) {
		return createBotScreenName(id);
	}

	@Override
	public boolean isTableJoinable(Table table) {
		boolean joinable = true;
		if (currency != null && !currency.equalsIgnoreCase("")) {
			joinable = currency.equalsIgnoreCase(table.getCurrency());
		}
		
		for (String key : lobbyAttributes.keySet()) {
			Param param = table.getAttributes().get(key);
			if (param != null) {
				Parameter<String> p = ParameterUtil.convertAsString(param);
				//log.info("--- CHECK "+key+":"+lobbyAttributes.get(key)+"  ->  "+p.getKey()+":"+p.getValue());
				if (p.getValue().equalsIgnoreCase(lobbyAttributes.get(key))) {
					log.debug("Lobby attribute matches, Filter("+key+":"+lobbyAttributes.get(key)+") == Table("+p.getKey()+":"+p.getValue()+")");
					joinable &= true;
				} else {
					log.debug("Lobby attribute does not match, skipping table "+table.getId()+":"+table.getName()+" , Filter("+key+":"+lobbyAttributes.get(key)+") != Table("+p.getKey()+":"+p.getValue()+")");
					joinable = false;
					break;
				}
			} else {
				log.debug("Missing lobby attribute "+key+". Will not join table "+table.getId()+":"+table.getName());
				joinable = false;
				break;
			}
			
		}
		
		return joinable;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getCurrency() {
		return currency;
	}

	@Override
	public void setLobbyAttributes(Map<String, String> lobbyAttributes) {
		this.lobbyAttributes = lobbyAttributes;
		
	}
}

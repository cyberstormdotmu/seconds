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

package com.cubeia.firebase.bot.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.util.ParameterUtil;
import com.cubeia.firebase.io.protocol.Param;

/**
 * This is a state-model for a table used by the AI Bots
 * 
 * @author Fredrik
 *
 */
public class Table {
	
	@SuppressWarnings("unused")
	private static transient Logger log = Logger.getLogger(Table.class);
	
	/** -1 means not seated (not applicable) */
	private int id = -1;
	private String name;
	private int	capacity = -1;
	private String address;
	private String currency;
	
	private ConcurrentMap<String, Param> attributes = new ConcurrentHashMap<String, Param>();
	
	/** Number of seated players, updated through lobby data only */
	private int seated = -1;
	
	/** Players at the table */
	private ConcurrentMap<Integer,Player> players = new ConcurrentHashMap<Integer,Player>();
	
	/** Position of the dealer button */
	private int button;

	public Table() {}
	
	public Table(int id, String name, int capacity, String address) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.address = address;
    }
	
	public ConcurrentMap<String, Param> getAttributes() {
		return attributes;
	}
	
	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public void addLocalPlayer(Player player) {
//	    log.debug("Table["+id+"] add player: "+player.getName()+" - "+player.getId());
		players.put(player.getId(), player);
	}
	
	public void removeLocalPlayer(int playerId) {
//	    log.debug("Table["+id+"] remove player: "+playerId);
	    players.remove(playerId);
	}

	public ConcurrentMap<Integer, Player> getLocalPlayers() {
		return players;
	}

	public void setLocalPlayers(ConcurrentMap<Integer, Player> players) {
		this.players = players;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSeated() {
		return seated;
	}

	public void setSeated(int seated) {
		this.seated = seated;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	public boolean isFull() {
		// First check lobby data model
		boolean full = seated == capacity;
		
		// Then check our own data
		full |= (players.size() >= capacity);
		// log.debug("Table["+id+"] full("+full+") in lobby("+seated+"/"+capacity+"). Players: "+players.size()+"/"+capacity);
		
		return full;
	}
	
	/**
	 * Checks if this table is joinable. A table is considered joinable
	 * if it the number of empty seats is larger than leaveEmptySeats.
	 * 
	 * For example, if leaveEmptySeats = 2, a table is joinable if it has 3 or more empty seats.
	 * 
	 * @param leaveEmptySeats
	 * @return
	 */
	public boolean isJoinable(int leaveEmptySeats) {
		
		boolean joinable = false;
		
		if (leaveEmptySeats <= 0) {
			joinable = !isFull();
		} else {
			joinable = countEmptySeats() > leaveEmptySeats;
		}
		
		return joinable;
	}
	
	private int countEmptySeats() {
		if (isFull()) {
			return 0;
		}
//		if (seated != players.size()) {
//			log.warn("seated[" + seated + "] != players.size()[" + players.size() + "], using players.size())");
//		}
		return capacity - players.size();
	}

	
	public String toString() {
		String table = "TABLE id["+id+"] name["+name+"] cap["+capacity+"] seated["+seated+"] PlayerSize["+players.size()+"] Players[ ";
		for (Integer seat : players.keySet()) {
			table += seat+":"+players.get(seat)+" ";
		}
		table += "] Attributes[ ";
		for (String key : attributes.keySet()) {
			table += key+"="+ParameterUtil.convert(attributes.get(key)).getValue()+" ";
		}
		table += "]";
		return table;
	}
	
	public String getAddress() {
        return address;
    }
}

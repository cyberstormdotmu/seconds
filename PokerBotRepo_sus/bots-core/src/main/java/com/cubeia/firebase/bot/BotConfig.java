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

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * 
 * Created on 2006-sep-20
 * @author Fredrik Johansson
 *
 * $RCSFile: $
 * $Revision: $
 * $Author: $
 * $Date: $
 */
public class BotConfig {
    
	/** Server address */
    private InetSocketAddress host = new InetSocketAddress("localhost", 4123);

    /** Starting id */
    private int startid = 1;
    
    /** Number of bots in the group (used by the creator) */
    private int size = 0;
    
    /** Id of the targetted group (if applicable) */
    private String groupid;

    private int gameId = -1;
    
    private boolean reconnect = true;
    
    // batch properties
    private Map<String,Integer> batchProperties;

    // batch flags
    private Map<String,Boolean> batchFlags;
    
    private Map<String, String> stringProperties;
    
    private String aiclass;
    
    private String groupConfigClass;
    
    private boolean tournamentBot = false;
    
    /** Valid types: mina || socket || websocket || comet */
    private String connectorType = "mina";
    
    private String currency;

	private Map<String, String> lobbyAttributes;

    public InetSocketAddress getHost() {
        return host;
    }

    public void setHost(InetSocketAddress address) {
        this.host = address;
    }

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStartid() {
		return startid;
	}

	public void setStartid(int startid) {
		this.startid = startid;
	}

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    /**
     * @return Returns the aiclass.
     */
    public String getAiclass() {
        return aiclass;
    }
    
    public String getGroupConfigClass() {
		return groupConfigClass;
	}
    
    public void setGroupConfigClass(String groupConfigClass) {
		this.groupConfigClass = groupConfigClass;
	}

    public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
     * @param aiclass The aiclass to set.
     */
    public void setAiclass(String aiclass) {
        this.aiclass = aiclass;
    }

	public Map<String, Boolean> getBatchFlags() {
		return batchFlags;
	}

	public void setBatchFlags(Map<String, Boolean> batchFlags) {
		this.batchFlags = batchFlags;
	}

	public Map<String, Integer> getBatchProperties() {
		return batchProperties;
	}

	public void setBatchProperties(Map<String, Integer> batchProperties) {
		this.batchProperties = batchProperties;
	}

    public boolean isTournamentBot() {
        return tournamentBot;
    }

    public void setTournamentBot(boolean tournamentBot) {
        this.tournamentBot = tournamentBot;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void setReconnect(boolean reconnect) {
        this.reconnect = reconnect;
    }

	public void setStringProperties(Map<String, String> stringProperties) {
		this.stringProperties = stringProperties;
	}

	public Map<String, String> getStringProperties() {
		return stringProperties;
	}
	
	public String getConnectorType() {
		return connectorType;
	}
	
	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Map<String, String> getLobbyAttributes() {
		return lobbyAttributes;
	}

	public void setLobbyAttributes(Map<String, String> lobbyAttributes) {
		this.lobbyAttributes = lobbyAttributes;
	}
}


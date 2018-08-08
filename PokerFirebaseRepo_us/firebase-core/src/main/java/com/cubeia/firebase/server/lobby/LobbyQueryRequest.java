/**
 * Copyright (C) 2011 Cubeia Ltd <info@cubeia.com>
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
package com.cubeia.firebase.server.lobby;

import com.cubeia.firebase.api.lobby.LobbyPath;
import com.cubeia.firebase.api.lobby.LobbyPathType;

/**
 * Models a user request for a query.
 * A Query is defined as a one-time snapshot request.
 *
 * @author Fredrik
 */
public class LobbyQueryRequest {
	
	private int playerid = -1;
	
	private LobbyPathType type = LobbyPathType.TABLES;

	private final LobbyPath path;
	
	public LobbyQueryRequest(int playerid, LobbyPath path) {
		super();
		this.playerid = playerid;
		this.path = path;
	}

	public LobbyPath getPath() {
		return path;
	}

	public int getPlayerid() {
		return playerid;
	}

	public LobbyPathType getType() {
		return type;
	}

	public void setType(LobbyPathType type) {
		this.type = type;
	}
	
	public String toString() {
		return "Lobby Query - pid["+playerid+"] fqn["+path+"] type["+type+"]";
	}
}

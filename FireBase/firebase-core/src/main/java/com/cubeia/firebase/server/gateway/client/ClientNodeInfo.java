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
package com.cubeia.firebase.server.gateway.client;

import java.io.Serializable;

import com.cubeia.firebase.service.messagebus.MBusDetails;

/**
 * This object is used to keep track of the node in which a client
 * is logged in. 
 * 
 * @author Larsan
 */
public final class ClientNodeInfo implements Serializable {

	private static final long serialVersionUID = -3676653936901618886L;
	
	private final MBusDetails mbus;
	private final String id;
	
	/**
	 * @param id Client node string id, must not be null
	 * @param mbus MBus details for the client node, must not be null
	 */
	public ClientNodeInfo(String id, MBusDetails mbus) {
		this.mbus = mbus;
		this.id = id;
	}
	
	/**
	 * @return The client node string id
	 */
	public String getNodeId() {
		return id;
	}
	
	/**
	 * @return The mbus details of the client node
	 */
	public MBusDetails getMBusDetails() {
		return mbus;
	}
}

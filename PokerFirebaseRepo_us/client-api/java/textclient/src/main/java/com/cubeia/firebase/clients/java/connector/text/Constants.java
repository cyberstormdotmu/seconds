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
package com.cubeia.firebase.clients.java.connector.text;

public class Constants {
	// Change Sushant
	//public static final boolean USE_SSL = System.getProperty("firebase.textclient.ssl", "false").equals("true");
	public static final boolean USE_SSL = System.getProperty("firebase.textclient.ssl", "true").equals("true");
	public static final boolean USE_NAIVE_SSL = System.getProperty("firebase.textclient.naive-ssl", "true").equals("true");
	public static final boolean USE_NATIVE = System.getProperty("firebase.textclient.native-encryption", "true").equals("true");
	public static final boolean USE_HANDSHAKE = System.getProperty("firebase.textclient.use-handshake", "false").equals("true");;
	public static final int HANDHAKE = Integer.parseInt(System.getProperty("firebase.textclient.handshake", "0"));

}

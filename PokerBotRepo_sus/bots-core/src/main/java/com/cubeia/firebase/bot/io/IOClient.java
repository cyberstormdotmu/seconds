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

package com.cubeia.firebase.bot.io;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.apache.mina.common.IoSession;

import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.protocol.PacketVisitor;

public interface IOClient {

	public void setConnectionListener(ConnectionListener connectionListener);

	public void addPacketHandler(PacketVisitor packetHandler);

	public void sendDataPacket(int tableid, int pid, ProtocolObject packet);

	public void wrapAndSendByteBuffer(int tableId, int pid, ByteBuffer buffer);

	public void sendPacket(ProtocolObject packet);

	public void connect(InetSocketAddress host, boolean processLobby);
	
	public void disconnect();
	
	public IoSession getSession();

}

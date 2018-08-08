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

package com.cubeia.firebase.bot.io.connectors.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.cubeia.firebase.bot.io.ConnectionListener;
import com.cubeia.firebase.bot.io.IOClient;
import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.StyxSerializer;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.PacketVisitor;

/**
 * {@link IoHandler} implementation.
 * 
 * Handles client sessions.
 * 
 * 
 */
public class MinaClient extends IoHandlerAdapter implements IOClient {

	private transient Logger log = Logger.getLogger(this.getClass());

	/** Packet handlers */
	private List<PacketVisitor> packetHandlers = new Vector<PacketVisitor>();

	private ConnectionListener connectionListener;

	private IoSession session;

	private StyxSerializer styxEncoder = new StyxSerializer(null);

	/*public MinaClient() {
		System.out.println("CREATED MINA CLIENT");
		new RuntimeException().printStackTrace();
	}*/

	/**
	 * Create a new GameClient object and attach it to the session.
	 * 
	 */
	@Override
	public void sessionCreated( IoSession session ) throws Exception {
		super.sessionCreated(session);
		this.session = session;
		if (log.isDebugEnabled()) {
			log.debug("Session created: "+session);
		}
		connectionListener.gotConnected();
	}

	@Override
	public void exceptionCaught( IoSession session, Throwable cause ) {
		log.error("Exception : "+cause, cause);
		if (!(cause instanceof IOException)) {
			log.info("Exception caught for session. Closing session: "+session, cause);
		}
		session.close();
	}

	/**
	 * Dispatch it to the attached GameClient
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void messageReceived( IoSession session, Object message ) {
		List<ProtocolObject> packets = (List<ProtocolObject>) message;
		for (ProtocolObject packet : packets) {
			for (PacketVisitor visitor : packetHandlers) {
				packet.accept(visitor);
			}
		}
	}



	@Override
	public void sessionOpened( IoSession session ) throws Exception { }

	@Override
	public void sessionClosed( IoSession session ) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Session closed: "+session.getAttachment());
		}
		connectionListener.gotDisconnected();
	}

	@Override
	public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Session idle (Will Remove): "+session.getAttachment()+" Status: "+status);
		}
		connectionListener.gotConnectionError();
	}

	@Override
	public void messageSent( IoSession session, Object message ) throws Exception {
	}


	// ------- BOT IO CLIENT METHODS ---------

	public void addPacketHandler(PacketVisitor packetHandler) {
		packetHandlers.add(packetHandler);
	}

	public void connect(InetSocketAddress host, boolean processLobby) {
		MinaConnector.getInstance().connect(host, this, processLobby);
	}

	public void disconnect() {
	    if (session != null) {
	        session.close();
	    }
	}

	public void sendDataPacket(int tableid, int pid, ProtocolObject packet) {
		//try {
			ByteBuffer buffer = styxEncoder.pack(packet);
			wrapAndSendByteBuffer(tableid, pid, buffer);
		//} catch(IOException ex){
		//	log.warn("IO error when sending. Will close session : "+ex);
		//	session.close();
		//}
	}

	public void sendPacket(ProtocolObject packet) {
	    if (session != null) {
	        session.write(packet);
	    }
	}

	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	/**
	 * Wraps the ByteBuffer data in a GameTransportPacket
	 * and sends it to the server.
	 * 
	 * @param table
	 * @param player
	 * @param buffer
	 */
	public void wrapAndSendByteBuffer(int tableId, int pid, ByteBuffer buffer) {
		GameTransportPacket gamePacket = new GameTransportPacket();
		gamePacket.tableid = tableId;
		gamePacket.pid = pid;

		gamePacket.gamedata = buffer.array();
		sendPacket(gamePacket);
	}
	
	public IoSession getSession() {
		return session;
	}

	// ------- END OF BOT IO CLIENT METHODS ---------


}
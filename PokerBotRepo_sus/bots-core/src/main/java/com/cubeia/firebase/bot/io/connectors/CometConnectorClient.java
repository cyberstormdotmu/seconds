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

package com.cubeia.firebase.bot.io.connectors;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.mina.common.IoSession;

import com.cubeia.firebase.bot.io.ConnectionListener;
import com.cubeia.firebase.bot.io.IOClient;
import com.cubeia.firebase.clients.java.connector.CometdConnector;
import com.cubeia.firebase.clients.java.connector.CometdConnectorFactory;
import com.cubeia.firebase.clients.java.connector.PacketListener;
import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.StyxSerializer;
import com.cubeia.firebase.io.protocol.GameTransportPacket;
import com.cubeia.firebase.io.protocol.PacketVisitor;
import com.cubeia.firebase.util.NamedThreadFactory;

/**
 * <p>IO Client that uses the Firebase Java Connector API for connections.</p>
 * 
 * <p>The connector API does not use thread pooling like MINA NIO, so there
 * will be one thread per connection. If this is an issue you should change
 * to the MinaClient implementation instead.</p> 
 * 
 * @author Fredrik Johansson, Cubeia Ltd
 *
 */
public class CometConnectorClient implements IOClient {

	private transient Logger log = Logger.getLogger(getClass());

	private CometdConnector connector;
	
	private ConnectionListener connectionListener;
	
	private StyxSerializer styxEncoder = new StyxSerializer(null);

	private List<PacketVisitor> packetHandlers = new ArrayList<PacketVisitor>();

	private static Executor executor = Executors.newCachedThreadPool(new NamedThreadFactory("cometd-connector-client"));
	
	private static CometdConnectorFactory factory;

	public CometConnectorClient() {
		
	}
	
	@Override
	public void connect(InetSocketAddress host, boolean processLobby) {
		log.info("Connect to: "+host.getHostName()+" : "+host.getPort());
		CometdConnectorFactory factory = getFactory(host);
		try {
			connector = (CometdConnector) factory.createConnector();
			connector.addListener(new FirebasePacketListener());
			connector.connect();
			connectionListener.gotConnected();
		} catch (Exception e) {
			System.err.println("Failed to connect: "+e);
			throw new RuntimeException(e);
		} 
	}

	private synchronized CometdConnectorFactory getFactory(InetSocketAddress host) {
		if (factory == null) {
			factory = new CometdConnectorFactory(host.getHostName(), host.getPort(), "/cometd");
			factory.setDispatcherExecutorServiceFactory(new SharedExecutorServiceFactory());
			factory.setExecutor(executor);
			try {
				factory.start();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return factory;
	}

	
	@Override
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	@Override
	public void addPacketHandler(PacketVisitor packetHandler) {
		packetHandlers.add(packetHandler);
	}

	@Override
	public void sendDataPacket(int tableid, int pid, ProtocolObject packet) {
		ByteBuffer buffer = styxEncoder.pack(packet);
		wrapAndSendByteBuffer(tableid, pid, buffer);
	}

	@Override
	public void wrapAndSendByteBuffer(int tableId, int pid, ByteBuffer buffer) {
		GameTransportPacket gamePacket = new GameTransportPacket();
		gamePacket.tableid = tableId;
		gamePacket.pid = pid;

		gamePacket.gamedata = buffer.array();
		sendPacket(gamePacket);
	}

	@Override
	public void sendPacket(ProtocolObject packet) {
		connector.send(packet);
	}

	
	@Override
	public synchronized void disconnect() {
		factory.stop();
	}

	@Override
	public IoSession getSession() {
		return null;
	}

	/**
	 * Adapter between the Bot packet visitor and the Java connector listener.
	 */
	private class FirebasePacketListener implements PacketListener {

		@Override
		public void packetRecieved(ProtocolObject packet) {
			for (PacketVisitor visitor : packetHandlers) {
				packet.accept(visitor);
			}
		}
		
	}
}

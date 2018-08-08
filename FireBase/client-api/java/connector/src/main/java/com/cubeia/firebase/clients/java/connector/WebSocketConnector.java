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
package com.cubeia.firebase.clients.java.connector;

import static com.cubeia.firebase.clients.java.connector.HttpConstants.HANDSHAKE_HTTP_HEADER;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.cubeia.firebase.io.ProtocolObject;
import com.cubeia.firebase.io.StyxJsonSerializer;
import com.cubeia.firebase.io.protocol.ProtocolObjectFactory;

// TODO Add Encryption
public class WebSocketConnector extends ConnectorBase {

	private final static StyxJsonSerializer serializer = new StyxJsonSerializer(new ProtocolObjectFactory());
	
	private final Logger log = Logger.getLogger(getClass());

    private static final int MAX_TEXT_BUFFER_SIZE_BYTES = 1024 * 1024;
	
	private final String host;
	private final int port;
	private final String path;
	
	// private int maxTextMessageSize = DEF_MAX_MESSAGE_SIZE;
	
	/*private WebSocketClient client;
	private WebSocketClientFactory factory;
	private WebSocket.Connection connection;*/
	
	private EventHandler eventHandler;
	private WebSocketClient client;
	private Future<Session> connectFuture;

	private Executor executor;
	
	// private ChannelFuture channelConnectionFuture;
	
	public WebSocketConnector(String host, int port, String path, boolean useHandshake, int handshakeSignature) {
		super(useHandshake, handshakeSignature);
		this.host = host;
		this.port = port;
		this.path = path;
	}
	
	public WebSocketConnector(String host, int port, String path) {
		this(host, port, path, false, -1);
	}

	@Override
	public void send(ProtocolObject packet) {
		try {
			String json = serializer.toJson(packet);
			eventHandler.sendString(json);
		} catch(Exception e) {
			log.error("failed to send event", e);
		}
	}

	/**
	 * If you set an executor then it will be used by the underlying Jetty Websocket client 
	 * as the treadpool for the socket handling. If you don't set it a default Threadpool
	 * of currently 8 threads will be created per client (this is design by Jetty not us).
	 * @param executor
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	@Override
	public void connect() throws IOException, GeneralSecurityException {
		super.connect();
		try {
			eventHandler = new EventHandler();
			
			client = new WebSocketClient();
			if (executor != null) {
			    client.setExecutor(executor);
			}
			
			client.getPolicy().setMaxTextMessageSize(MAX_TEXT_BUFFER_SIZE_BYTES);
			client.start();
			URI uri = createUri();
			ClientUpgradeRequest request = new ClientUpgradeRequest();
            connectFuture = client.connect(eventHandler, uri, request);
		} catch (Exception e) {
			throw new IOException("failed to connect websocket", e);
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();
		try {
		    if (eventHandler.session != null) {
		        eventHandler.session.close();
		    } else {
		        log.warn("session was null, but we tried to close it");
		    }
		    client.stop();
			client = null;
		} catch (Exception e) {
			log.error("failed to disconnect websocket", e);
		}
	}

	@Override
	public boolean isConnected() {
		Session ses = null;
		try {
			ses = connectFuture.get(1000, MILLISECONDS);
		} catch (TimeoutException e) {
			return false;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			throw new IllegalStateException(e);
		}
		return client != null && ses != null; 
	}

	/*public void setMaxTextMessageSize(int maxTextMessageSize) {
		this.maxTextMessageSize = maxTextMessageSize;
	}
	
	public int getMaxTextMessageSize() {
		return maxTextMessageSize;
	}*/
	
	
	// --- PRIVATE METHODS --- //
	
	private URI createUri() throws URISyntaxException {
		if(useHandshake) {
			return new URI("ws://" + host + ":" + port + path + "?" + HANDSHAKE_HTTP_HEADER + "=" + handshakeSignature);
		} else {
			return new URI("ws://" + host + ":" + port + path);
		}
	}
	
	
	// --- PRIVATE CLASSES --- //
	
	@WebSocket
	public class EventHandler {

		private CountDownLatch latch = new CountDownLatch(1);
		
		private Session session;
		
		public void waitForConnect() throws InterruptedException {
			latch.await();
		}
		
		@OnWebSocketClose
	    public void onClose(int statusCode, String reason) {
			log.debug("Websocket closed, reason: " + reason);
	        this.session = null;
	    }
	 
	    public void sendString(String json) throws IOException, InterruptedException {
	    	waitForConnect();
			session.getRemote().sendString(json);
		}

		@OnWebSocketConnect
	    public void onConnect(Session session) {
	        log.debug("Websocket connected, session is open: " + session.isOpen());
	        this.session = session;
	        latch.countDown();
	    }
	 
	    @OnWebSocketMessage
	    public void onMessage(String json) {
	    	if(json != null && json.length() > 0) {
				if(json.trim().startsWith("{")) {
					doFinalDispatch(serializer.fromJson(json));
				} else {
					for (ProtocolObject o : serializer.fromJsonList(json)) {
						doFinalDispatch(o);
					}
				}
			}
	    }
		
		private void doFinalDispatch(final ProtocolObject packet) {
			dispatcher.submit(new Runnable() {
			
				public void run() {
					for (PacketListener v : listeners) {
						v.packetRecieved(packet);
					}
				}
			});
		}
	}
}

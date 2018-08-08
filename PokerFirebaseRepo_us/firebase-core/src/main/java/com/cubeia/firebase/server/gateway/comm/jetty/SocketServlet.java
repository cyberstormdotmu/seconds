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
package com.cubeia.firebase.server.gateway.comm.jetty;

import static com.cubeia.firebase.server.gateway.GatewayNode.CLIENT_GATEWAY_NAMESPACE;
import static com.cubeia.firebase.server.gateway.comm.jetty.HttpUtil.checkHandshake;
import static com.cubeia.firebase.server.gateway.comm.jetty.HttpUtil.createSocketAddress;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.cubeia.firebase.api.service.config.ClusterConfigProviderContract;
import com.cubeia.firebase.io.protocol.Enums.ForcedLogoutCode;
import com.cubeia.firebase.io.protocol.ForcedLogoutPacket;
import com.cubeia.firebase.server.gateway.comm.config.GatewayClusterConfig;
import com.cubeia.firebase.server.node.ClientNodeContext;

/**
 * This is a web socket client layer. It uses the JSON wire format 
 * of Styx packets exclusively.
 * 
 * <p>This servlet accepts both single JSON encoded protocol objects as well as a list of the same, 
 * and it will always write a list of objects.
 * 
 * <p>The upgrade request must provide a handshake signature if the server
 * is configured with one. This is done via the "X-Cubeia-Firebase-Handshake" 
 * request header (alternatively if can be set in a cookie, or a request parameter, 
 * with the same name).
 * 
 * @author Lars J. Nilsson
 */
public class SocketServlet extends WebSocketServlet {

	private static final long serialVersionUID = 33977554975636417L;

	private final Logger log = Logger.getLogger(getClass());
	private final ClientNodeContext context;
	private String handShake; // handshake signature, null if not used
	
	public SocketServlet(ClientNodeContext context) {
		this.context = context;
		setupHandshake();
	}

	public boolean checkOrigin(ServletUpgradeRequest request, String origin) {
		boolean b = checkHandshake(request, handShake);
		if(b) {
			log.trace("Correct handshake signature received from: " + createSocketAddress(request));
		} else {
			log.debug("Incorrect handshake signature received from: " + createSocketAddress(request));
		}
		return b;
	}
	

	// --- PRIVATE METHODS --- //
	
	private void setupHandshake() {
        ClusterConfigProviderContract clusterConfigService = context.getServices().getServiceInstance(ClusterConfigProviderContract.class);
		GatewayClusterConfig config = clusterConfigService.getConfiguration(GatewayClusterConfig.class, CLIENT_GATEWAY_NAMESPACE);
		if(config.isHandshakeEnabled()) {
			log.info("SocketServlet enabling handshake with signature: " + config.getHandshakeSignature());
			handShake = String.valueOf(config.getHandshakeSignature());
		}
	}
	
	
	// --- PRIVATE CLASSES --- //
	
	private class SocketCreator implements WebSocketCreator {

		@WebSocket
		public class Socket extends BaseSocketConnection {

			private Session session;

			public Socket(AsyncSession session, InetSocketAddress ia) {
				super(session, ia, true);
			}
			
			@Override
			public void forceClose() {
                ForcedLogoutPacket packet = new ForcedLogoutPacket();
                // TODO: change to non hardcoded values
                packet.code = ForcedLogoutCode.IDLE_TIMEOUT.ordinal();
                packet.message = "WebSocket idle timeout";
                try {
                    // send packet
                    doSend(JsonUtil.pack(packet));
                } catch (IOException e) {
                    // if we end up here, the send failed, nothing we can do anything about
                    // requires a modified jetty-websocket to work
                }
			    
				try {
				    if (session.isOpen()) {
				        session.close();
				    }
				} catch (Exception e) {
					log.error("Error closing websocket session", e);
				}
			}

			@Override
			public void doSend(String msg) throws IOException {
				session.getRemote().sendStringByFuture(msg);
			}
			
			@OnWebSocketConnect
			public void onConnect(Session session) {
			    log.debug("websocket connected");
				this.session = session;
				super.doOpen();
			}
			
			@OnWebSocketMessage
			public void onMessage(String data) {
				super.doMessage(data);
			}

			@OnWebSocketClose
			public void onClose(int closeCode, String message) {
			    /* FIXME: Calling send inside onClose causes exception as the state is CLOSING
				if ( closeCode == 1000 ) {
			        ForcedLogoutPacket packet = new ForcedLogoutPacket();
					// TODO: change to non hardcoded values
					packet.code = ForcedLogoutCode.IDLE_TIMEOUT.ordinal();
					packet.message = "WebSocket idle timeout";
					try {
				        // send packet
						doSend(JsonUtil.pack(packet));
					} catch (IOException e) {
						// if we end up here, the send failed, nothing we can do anything about
						// requires a modified jetty-websocket to work
				    }
				}
				*/
				super.doClose();
			}
		}
		
		

		@Override
		public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
		    log.debug("websocket upgrade request");
		    
			if (checkOrigin(request, handShake)) {
    			InetSocketAddress address = request.getRemoteSocketAddress();
    			JettyAsyncClient client = new JettyAsyncClient(context, address);
    			return new Socket(client , address);
			} else {
			    return null;
			}
		}
		
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new SocketCreator());
		
	}
}

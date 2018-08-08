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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;

/**
 * <p>CometD connector factory.</p>
 * 
 * <p>Default connect time out is 10 seconds. Change this by setting setConnectTimeout(milliseconds).</p>
 * 
 */
public class CometdConnectorFactory extends BaseConnectorFactory {

	private HttpClient client;
	
	private final String host;
	private final int port;
	private final String context;
	
	private int connectTimeout = 10000;
	private Executor executor;
	
	public CometdConnectorFactory(String host, int port, String context) {
		this.host = host;
		this.port = port;
		this.context = context;
	}
	
	@Override
	public void start() throws IOException {
		try {
			client = createClient();
			client.start();
		} catch (Exception e) { 
			throw new IOException("failed to start http client", e);
		}
	}
	
	protected HttpClient createClient() {
		HttpClient client = new HttpClient();
		client.setMaxConnectionsPerDestination(4096);
		
		if (executor != null) {
			client.setExecutor(executor);
		}
		
		client.setConnectTimeout(connectTimeout);
		
		return client;
	}
	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	@Override
	protected Connector doCreateConnector(SecurityConfig conf) throws IOException, GeneralSecurityException {
		if(conf == null) {
			return new CometdConnector(client, host, port, context, false, -1);
		} else {
			return new CometdConnector(client, host, port, context, conf.useHandshake, conf.handshakeSignature);
		}
	}
	
	@Override
	public void stop() {
		try {
			client.stop();
		} catch (Exception e) {
			Logger.getLogger(getClass()).error("failed to stop http client", e);
		}
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}

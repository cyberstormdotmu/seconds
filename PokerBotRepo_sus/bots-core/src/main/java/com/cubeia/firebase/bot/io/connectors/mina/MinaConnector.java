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

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.apache.mina.filter.SSLFilter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.integration.jmx.IoServiceManager;
import org.apache.mina.transport.socket.nio.SocketConnector;
import org.apache.mina.transport.socket.nio.SocketConnectorConfig;

import com.cubeia.firebase.bot.mina.styx.StyxProtocolFactory;

/**
 * Singleton connector
 *
 * @author Fredrik
 */
public class MinaConnector {
	
	private static transient Logger log = Logger.getLogger(MinaConnector.class);
	
	private static MinaConnector instance = new MinaConnector();
	private SocketConnector connector;
	
	private MinaConnector() {
        connector = new SocketConnector();
        SSLFilter sslFilter = new SSLFilter(NewSslContextFactory.getInstance(true));
        sslFilter.setUseClientMode(true);
        connector.getFilterChain().addFirst("sslFilter", sslFilter);
        connector.getDefaultConfig().setConnectTimeout(10);
        initJMX();
	};
	
	public static MinaConnector getInstance() {
		return instance;
	}
	
	public void connect(InetSocketAddress address, MinaClient handler, boolean processLobby) {
		SocketConnectorConfig cfg = new SocketConnectorConfig();
		cfg.getFilterChain().addLast("codec", new ProtocolCodecFilter(new StyxProtocolFactory(processLobby)));
		
		cfg.setConnectTimeout(10);
		
		connector.connect(address, handler, cfg);
	}
	
	
	private void initJMX() {
		try {
			IoServiceManager serviceManager = new IoServiceManager( connector );
			serviceManager.startCollectingStats(1000);
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			ObjectName name = new ObjectName("com.cubeia.bot.mina:type=Server");
			mbs.registerMBean(serviceManager, name);
		} catch (Exception e) {
			log.warn("Could not bind MServer to JMX", e);
		}
	}
	
}

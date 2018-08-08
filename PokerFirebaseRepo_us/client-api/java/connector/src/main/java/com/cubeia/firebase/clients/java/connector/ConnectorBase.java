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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

import com.cubeia.firebase.api.util.Arguments;

public abstract class ConnectorBase implements Connector {

	protected final Logger log = Logger.getLogger(getClass());
	protected ExecutorService dispatcher; //  = Executors.newSingleThreadExecutor();
	// protected static final ExecutorService dispatcher = Executors.newCachedThreadPool(new NamedThreadFactory("connector-dispatcher"));
	protected final List<PacketListener> listeners = new CopyOnWriteArrayList<PacketListener>();
	protected final boolean useHandshake;
	protected final int handshakeSignature;
	
	protected ExecutorServiceFactory dispatcherFactory = new IsolatedSingleThreadExectorServiceFactory();
	
	protected ConnectorBase(boolean useHandshake, int handshakeSignature) {
		this.useHandshake = useHandshake;
		this.handshakeSignature = handshakeSignature;
	}
	
	@Override
	public void connect() throws IOException, GeneralSecurityException {
		dispatcher = dispatcherFactory.openForConnector(this);
	}
	
	@Override
	public void disconnect() {
		dispatcherFactory.closeForConnector(this, dispatcher);
	}
	
	@Override
	public void setDispatcherExecutorServiceFactory(ExecutorServiceFactory factory) {
		dispatcherFactory = factory;
	}
	
	public void addListener(PacketListener handler) {
		Arguments.notNull(handler, "handler");
		listeners.add(handler);
	}

	public void removeListener(PacketListener handler) {
		Arguments.notNull(handler, "handler");
		listeners.remove(handler);
	}
}

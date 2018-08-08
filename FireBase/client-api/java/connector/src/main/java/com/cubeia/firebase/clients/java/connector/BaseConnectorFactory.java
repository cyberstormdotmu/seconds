package com.cubeia.firebase.clients.java.connector;

import java.io.IOException;
import java.security.GeneralSecurityException;

public abstract class BaseConnectorFactory implements ConnectorFactory {

	private ExecutorServiceFactory dispatcherFactory;

	@Override
	public void start() throws IOException { }

	@Override
	public void setDispatcherExecutorServiceFactory(ExecutorServiceFactory factory) {
		this.dispatcherFactory = factory;
	}

	@Override
	public Connector createConnector() throws IOException, GeneralSecurityException {
		return createConnector(null);
	}

	@Override
	public Connector createConnector(SecurityConfig conf) throws IOException, GeneralSecurityException {
		Connector c = doCreateConnector(conf);
		if(dispatcherFactory != null) {
			c.setDispatcherExecutorServiceFactory(dispatcherFactory);
		}
		return c;
	}

	/**
	 * @param conf Config to use, may be null
	 * @return A new connector, never null
	 */
	protected abstract Connector doCreateConnector(SecurityConfig conf) throws IOException, GeneralSecurityException;

	@Override
	public void stop() { }

}

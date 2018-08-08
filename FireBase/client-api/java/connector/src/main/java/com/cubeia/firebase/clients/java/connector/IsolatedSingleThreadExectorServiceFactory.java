package com.cubeia.firebase.clients.java.connector;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.cubeia.util.threads.NamedThreadFactory;

/*
 * one thread per connector
 */
public class IsolatedSingleThreadExectorServiceFactory implements ExecutorServiceFactory {

	private final ThreadFactory factory;
	
	public IsolatedSingleThreadExectorServiceFactory(ThreadFactory factory) {
		this.factory = factory;
	}
	
	public IsolatedSingleThreadExectorServiceFactory() {
		this(new NamedThreadFactory("connector-dispatcher"));
	}

	@Override
	public ExecutorService openForConnector(Connector c) {
		return Executors.newSingleThreadExecutor(factory);
	}

	@Override
	public void closeForConnector(Connector c, ExecutorService service) {
		service.shutdown();
	}
}

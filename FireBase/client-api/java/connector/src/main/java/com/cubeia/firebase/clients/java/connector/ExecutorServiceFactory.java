package com.cubeia.firebase.clients.java.connector;

import java.util.concurrent.ExecutorService;

public interface ExecutorServiceFactory {

	public ExecutorService openForConnector(Connector c);
	
	public void closeForConnector(Connector c, ExecutorService service);
	
}

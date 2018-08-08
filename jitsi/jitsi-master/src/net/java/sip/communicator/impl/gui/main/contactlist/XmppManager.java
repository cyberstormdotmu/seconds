package net.java.sip.communicator.impl.gui.main.contactlist;


import java.io.IOException;
import java.net.InetAddress;

import net.java.sip.communicator.service.gui.XmppConnector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XmppManager {

	private static final int packetReplyTimeout = 500;
    private String server;

    private ConnectionConfiguration config;
    
    public XMPPConnection getConnection() {
		return connection;
	}


	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}


	private XMPPConnection connection;

    private ChatManager chatManager;

    public XmppManager(String server){
    	this.server = server;
    }

    ChatManager manager = null;
    private MyMessageListener listener;
    
    public void init() throws XMPPException, IOException, InterruptedException{
    	SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
    	config = new ConnectionConfiguration(server);
    	config.setSASLAuthenticationEnabled(true);
    	config.setSecurityMode(SecurityMode.disabled);
    	connection = XmppConnector.connection;
    	
    	if(!connection.isConnected()){
    	    connection.connect();
    	}


    	InetAddress inet = InetAddress.getByName(server);

    	chatManager = connection.getChatManager();

    	 manager = connection.getChatManager();
    }
    

    public void destroy() {
        if (connection!=null && connection.isConnected()) {
            connection.disconnect();
        }
    }


    public void sendMessage(String message, String buddyJID) throws XMPPException {
        Chat chat = manager.createChat(buddyJID,listener);

        chat.sendMessage(message);
    }

}

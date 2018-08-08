package net.java.sip.communicator.impl.gui.main.contactlist;

import net.java.sip.communicator.service.gui.XmppConnector;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class CustomSendMessage
    implements MessageListener
{

    XMPPConnection connection;
    
    public void login(String userName, String password) throws XMPPException
    {
    connection = XmppConnector.connection;
}

    public void sendMessage(String message, String to) throws XMPPException{
        Chat chat = connection.getChatManager().createChat(to, this);
        chat.sendMessage(message);
    }
    
    @Override
    public void processMessage(Chat paramChat, Message paramMessage){
    }

}

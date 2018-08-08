package net.java.sip.communicator.service.gui;


import net.java.sip.communicator.impl.protocol.jabber.ServerStoredContactListJabberImpl;
import net.java.sip.communicator.service.protocol.ProtocolProviderService;

import org.jivesoftware.smack.XMPPConnection;

/*
 * @author TatvaSoft
 */

public class XmppConnector
{
    public static XMPPConnection connection;
    public static String xmppUserName;
    public static String password;
    public static ServerStoredContactListJabberImpl sslCallback;
    public static ProtocolProviderService protocolService;
    public static boolean isMerging = false;
    public static int mergingThreads = 0;
}

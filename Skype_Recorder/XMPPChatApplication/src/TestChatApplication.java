import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class TestChatApplication {
 
	// XMPP Connection variable declaration
    XMPPConnection connection;
 
    public static void main(String args[]) throws XMPPException, IOException
    {
    // declare variables
    	ChatLogic chatLogic = new ChatLogic();
    	BufferedReader buffrereader = new BufferedReader(new InputStreamReader(System.in));
    	String msg;
 
    // turn on the debugger
    XMPPConnection.DEBUG_ENABLED = true;

    // Enter User's login information here
    chatLogic.login("nirav.dhinoja@centos.tatva.com", "nirav123");
 
    chatLogic.displayFriendList();		//See the Online Friends
 
    System.out.println("-----");
    System.out.println("Hello Nirav, You are now online");
    System.out.println("Type full email-address of your friends to talk with them:");
    
    String talkTo = buffrereader.readLine();   
 

    System.out.println("-----");
    System.out.println("You are now linked with :" + talkTo);
    System.out.println("You can end chat by typing : 'TTL'");
    System.out.println("Start Chat From Here:");
    System.out.println("-----\n");
 
    while( !(msg=buffrereader.readLine()).equals("TTL"))   // check if conversation-end message not generated
    {
    	chatLogic.sendMessage(msg, talkTo);
    }
 
    chatLogic.disconnect();
    System.exit(0);
    }
 
}
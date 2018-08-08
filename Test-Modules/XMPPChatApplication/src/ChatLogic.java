
import java.io.File;
import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

public class ChatLogic implements MessageListener {

	// Global declaration for connection & 
	
        XMPPConnection connection;      
        private FileTransferManager manager;

        
        
        /** Establish the connection 
         * 
         * @param connection
         */
        public void setConnection(XMPPConnection connection) {
                this.connection = connection;
                manager = new FileTransferManager(connection);
        }

        
        public ChatLogic() {
        }

        
        /** 
         * Checking the User Authentication with existing chat server
         * @param userName
         * @param password
         * @throws XMPPException
         */
        public void login(String userName, String password) throws XMPPException {
                ConnectionConfiguration config = new ConnectionConfiguration(
                                "192.168.0.6", 5222, "chatApplication");
                connection = new XMPPConnection(config);

                connection.connect();
                connection.login(userName, password);
        }

        
        /** 
         * Setting the file transfer 
         * @param fileName
         * @param destination
         * @throws XMPPException
         */
        public void fileTransfer(String fileName, String destination)
                        throws XMPPException {

                // Create the file transfer manager
                 FileTransferManager manager = new FileTransferManager(connection);
                FileTransferNegotiator.setServiceEnabled(connection, true);
                // Create the outgoing file transfer
                OutgoingFileTransfer transfer = manager
                                .createOutgoingFileTransfer(destination);

                
                // Send the file
                transfer.sendFile(new File(fileName), "Bingo..! You has sent the File");
                try {
                        Thread.sleep(10000);
                } catch (Exception e) {
                }

                System.out.println("Status :: " + transfer.getStatus() + " Error :: "
                                + transfer.getError() + " Exception :: "
                                + transfer.getException());
                System.out.println("Is it done? " + transfer.isDone());
        }

        
        /**
         * binding message & destination for communication
         * @param message
         * @param to
         * @throws XMPPException
         */
        public void sendMessage(String message, String to) throws XMPPException {
                Chat chat = connection.getChatManager().createChat(to, this);
                chat.sendMessage(message);
        }

        
        /**
         * Displaying the Online Friends Which were added to friend list via Spark client
         */
        public void displayFriendList() {
                Roster roster = connection.getRoster();
                Collection<RosterEntry> entries = roster.getEntries();

                System.out.println("\n\n" + entries.size() + " Friends:");
                for (RosterEntry r : entries) {
                        System.out.println(r.getName());
                }
        }

        
        /**
         * for Ending the chat
         */
        public void disconnect() {
                connection.disconnect();
        }

        /**
         * Receiving the message
         */
        public void processMessage(Chat chat, Message message) {
                if (message.getType() == Message.Type.chat)
                        System.out.println(chat.getParticipant() + " says: "
                                        + message.getBody());
        }
}
package net.java.sip.communicator.impl.protocol.jabber;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.java.sip.communicator.service.gui.XmppConnector;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.packet.Presence;

public class GroupLoader{
    
    Roster rs = XmppConnector.connection.getRoster();
    
    public List<RosterGroup> getAllGroups(String clickedGroupName){

        List<RosterGroup> list = new ArrayList<RosterGroup>();

        for(RosterGroup group : rs.getGroups()){
            if(group.getName().equals(clickedGroupName)){
                list.add(0, group);
            }else{
                list.add(group);
            }
                
        }
        Presence pr ; 
        Collection<RosterEntry> contacts = rs.getEntries();
            for(RosterEntry entry:contacts){
                pr = rs.getPresence(entry.getUser());
                String mode=null;

                if(pr.getMode() != null)
                     mode = pr.getMode().name().toString();
            }
        /*String[] stringName = new String[groupName.size()];
            groupName.toArray(stringName);
            try
            {
                rs.createEntry("harshal.chavda@centos.tatva.com", "HARSHAL", stringName);
            }
            catch (XMPPException e)
            {
                e.printStackTrace();
            }*/
        return list;
    }
    // CheckFont fontColor = new CheckFont();
    
  public Font getStatus(String user){
        
      Font onlineFont = new Font("Serif", Font.BOLD, 14);
      Font normalFont = new Font("Serif", Font.PLAIN, 14);
        
          Presence pr = rs.getPresence(user);    
          String type=null;
          if(pr.getType()!=null)
            type = pr.getType().name().toString();
          else
            type=null;
          if(type.substring(0, 4).contains("un")){
            return normalFont;
          }else{
            /*if(pr.getMode() == null){
                return onlineFont;
            }else if(pr.getMode().name().equals("xa")){
                return onlineFont;
            }else if(pr.getMode().name().equals("dnd")){
                return onlineFont;
            }else{
                return onlineFont;
            }*/
              return onlineFont;
        }
    }
}

package net.java.sip.communicator.impl.gui.main.menus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.java.sip.communicator.service.gui.XmppConnector;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImportXml{
        
    

    
    public void parseXML(String path) throws XMPPException{
        try
        {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
            Document document;
            document = db.parse(new File(path));
            NodeList nodeList = document.getElementsByTagName("Contact");
            Roster rs = XmppConnector.connection.getRoster() ;
            if(nodeList.getLength() == 0){
                JOptionPane.showMessageDialog(null, "Provide Proper XML File !!");
            }else{
                
                try{
                    List<String> listgroup = new ArrayList<String>();
                    for(int x=0,size= nodeList.getLength(); x<size; x++) {
                        /*listgroup.add(nodeList.item(x).getAttributes().getNamedItem("parent-proto-group-uid").getNodeValue());
                        
                        System.out.println(nodeList.item(x).getAttributes().getNamedItem("address").getNodeValue());
                        System.out.println(nodeList.item(x).getAttributes().getNamedItem("parent-proto-group-uid").getNodeValue());
                        
                        String[] group = new String[listgroup.size()];
                        group = listgroup.toArray(group);
                        String name = nodeList.item(x).getAttributes().getNamedItem("address").getNodeValue();*/
                        
                        
                        Node nNode = nodeList.item(x);
                        if(nNode.getNodeType() == Node.ELEMENT_NODE){
                            Element ele = (Element) nNode;
                            String jid   = ele.getElementsByTagName("jid").item(0).getTextContent();
                            String name  = ele.getElementsByTagName("name").item(0).getTextContent();
                            String group = ele.getElementsByTagName("group").item(0).getTextContent();
                            System.out.println("JID >>"+ele.getElementsByTagName("jid").item(0).getTextContent());
                            System.out.println("Name>>"+ele.getElementsByTagName("name").item(0).getTextContent());
                            System.out.println("Group>>"+ele.getElementsByTagName("group").item(0).getTextContent());
                            
                            RosterEntry re = rs.getEntry(jid);
                            
                                if(re == null){
                                    listgroup.add(group);
                                    String[] groups = new String[listgroup.size()];
                                    groups = listgroup.toArray(groups);
                                    rs.createEntry(jid, name, groups);
                                    listgroup.clear();
                                    JOptionPane.showMessageDialog(null, "Successfully Imported All Contacts!! ");
                                }
                        }
                        // rs.createEntry(name,name, group);
                    }

                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "Provide Proper XML File !!");
                    e.printStackTrace();
                }
            }
            
           
            
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException | IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void exportXML(String path){
        Roster rs = XmppConnector.connection.getRoster() ; 
        Collection<RosterEntry> contacts = rs.getEntries();
        List<ExportRosterEntryModel> list = new ArrayList<ExportRosterEntryModel>();
        for(RosterEntry entry:contacts){
            ExportRosterEntryModel ex = new ExportRosterEntryModel();
            ex.setUserId(entry.getUser());
            ex.setUserName(entry.getName());
                if(ex.getUserName() == null){
                    ex.setUserName(ex.getUserId());
                }
            for(RosterGroup rg : entry.getGroups()){
                ex.setGroupName(rg.getName());
            }    
            if(ex.getGroupName() == null){
                ex.setGroupName("Contacts");
            }
            list.add(ex);
        }
        ExportFile exp = new ExportFile();
         if(exp.getExport(list,path))
             JOptionPane.showMessageDialog(null, "Successfully Exported All Contacts!! "); 
    }
    
}

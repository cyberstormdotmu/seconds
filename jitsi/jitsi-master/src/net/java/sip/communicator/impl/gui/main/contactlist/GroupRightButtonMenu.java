/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.sip.communicator.impl.gui.main.contactlist;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import net.java.sip.communicator.impl.contactlist.MetaContactListServiceImpl;
import net.java.sip.communicator.impl.gui.GuiActivator;
import net.java.sip.communicator.impl.gui.event.PluginComponentEvent;
import net.java.sip.communicator.impl.gui.event.PluginComponentListener;
import net.java.sip.communicator.impl.gui.main.MainFrame;
import net.java.sip.communicator.impl.gui.main.chat.ChatSession;
import net.java.sip.communicator.impl.gui.utils.ImageLoader;
import net.java.sip.communicator.impl.protocol.jabber.GroupLoader;
import net.java.sip.communicator.plugin.desktoputil.SIPCommMenu;
import net.java.sip.communicator.plugin.desktoputil.SIPCommPopupMenu;
import net.java.sip.communicator.service.contactlist.MetaContact;
import net.java.sip.communicator.service.contactlist.MetaContactGroup;
import net.java.sip.communicator.service.gui.Container;
import net.java.sip.communicator.service.gui.PluginComponent;
import net.java.sip.communicator.service.gui.PluginComponentFactory;
import net.java.sip.communicator.service.gui.XmppConnector;
import net.java.sip.communicator.util.ConfigurationUtils;
import net.java.sip.communicator.util.Logger;
import net.java.sip.communicator.util.skin.Skinnable;

import org.jivesoftware.smack.RosterGroup;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * The GroupRightButtonMenu is the menu, opened when user clicks with the right
 * mouse button on a group in the contact list. Through this menu the user could
 * add a contact to a group.
 *
 * @author Yana Stamcheva
 * @author Adam Netocny
 */
public class GroupRightButtonMenu
    extends SIPCommPopupMenu
    implements  ActionListener,
                PluginComponentListener,
                Skinnable
{
    private final Logger logger = Logger.getLogger(GroupRightButtonMenu.class);

    private final JMenuItem addContactItem
        = new JMenuItem(GuiActivator.getResources().getI18NString(
            "service.gui.ADD_CONTACT") + "...");

    private final JMenuItem removeGroupItem
        = new JMenuItem(GuiActivator.getResources().getI18NString(
            "service.gui.REMOVE_GROUP"));

    private final JMenuItem renameGroupItem
        = new JMenuItem(GuiActivator.getResources().getI18NString(
            "service.gui.RENAME_GROUP"));
    
   // TatvaSoft
    
    private final JMenuItem sendBroadcastMessageItem
        = new JMenuItem(GuiActivator.getResources().getI18NString(
            "service.gui.BROADCAT_MESSAGE"));
        
    private final SIPCommMenu moveToMenu
    = new SIPCommMenu(GuiActivator.getResources()
        .getI18NString("service.gui.MERGE_TO_GROUP"));

    private static final String moveToPrefix = "moveTo:";
    
    private ChatSession chatSession;
    
    private final MetaContactGroup group;

    private final MainFrame mainFrame;
    
    private CustomSendMessage cs;
    private BroadcastUI uiLoader;
    
    
    static boolean isAlreadyOpened = false;
    
     

    /**
     * Creates an instance of GroupRightButtonMenu.
     *
     * @param mainFrame The parent <tt>MainFrame</tt> window.
     * @param group The <tt>MetaContactGroup</tt> for which the menu is opened.
     */
    
    public GroupRightButtonMenu(){
        this.group = null;
        this.mainFrame = null;
    }
    
    public GroupRightButtonMenu(MainFrame mainFrame, MetaContactGroup group)
    {
        this.group = group;
        this.mainFrame = mainFrame;

        if (!ConfigurationUtils.isAddContactDisabled())
        {
            this.add(addContactItem);
            this.addSeparator();
        }

        if (!ConfigurationUtils.isGroupRenameDisabled())
        {
            this.add(renameGroupItem);
        }

        if (!ConfigurationUtils.isGroupRemoveDisabled())
        {
            this.add(removeGroupItem);
        }
        this.add(sendBroadcastMessageItem);
        moveToMenu.setIcon(new ImageIcon(ImageLoader
            .getImage(ImageLoader.MERGE_GROUP)));
        this.add(moveToMenu);
        
        
        this.addContactItem.setMnemonic(GuiActivator.getResources()
            .getI18nMnemonic("service.gui.ADD_CONTACT"));

        this.renameGroupItem.setMnemonic(GuiActivator.getResources()
            .getI18nMnemonic("service.gui.RENAME_GROUP"));

        this.removeGroupItem.setMnemonic(GuiActivator.getResources()
            .getI18nMnemonic("service.gui.REMOVE_GROUP"));

        this.addContactItem.addActionListener(this);
        this.renameGroupItem.addActionListener(this);
        this.removeGroupItem.addActionListener(this);
        this.sendBroadcastMessageItem.addActionListener(this);
            
        loadSkin();

        this.initPluginComponents();
        init();
    }
    
    /**
     * Initializes the menu, by adding all containing menu items.
     */
    private void init(){
        XmppConnector.isMerging = true;
        //Initialize moveTo menu.
        Iterator<MetaContactGroup> groups
            = GuiActivator.getContactListService().getRoot().getSubgroups();

        if(groups.hasNext())
        {
            JLabel infoLabel = new JLabel(
                GuiActivator.getResources()
                    .getI18NString("service.gui.SELECT_GROUP"));

            infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
            infoLabel.setFont(infoLabel.getFont().deriveFont(Font.BOLD));

            this.moveToMenu.add(infoLabel);
            this.moveToMenu.addSeparator();
        }

        if(GuiActivator.getContactList().getRootUIGroup() != null)
        {
            // adds contacts group if it is visible
            JMenuItem menuItem = new JMenuItem(
                GuiActivator.getContactList().getRootUIGroup()
                    .getDisplayName());
            menuItem.setName(moveToPrefix
                + GuiActivator.getContactListService().getRoot().getMetaUID());
            menuItem.addActionListener(this);

            this.moveToMenu.add(menuItem);
        }

        while (groups.hasNext())
        {
            MetaContactGroup currentGroup = groups.next();

            if (!currentGroup.isPersistent())
                continue;

            JMenuItem menuItem = new JMenuItem(currentGroup.getGroupName());

            menuItem.setName(moveToPrefix + currentGroup.getMetaUID());
            menuItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent paramActionEvent)
                {
                       XmppConnector.isMerging = true;
                        MetaContactGroup selectedGroup = null;
                        Iterator<MetaContactGroup> groups
                        = GuiActivator.getContactListService().getRoot().getSubgroups();
                        while(groups.hasNext()){
                            MetaContactGroup currentGroup = groups.next();
                                if(currentGroup.getGroupName().equals(paramActionEvent.getActionCommand())){
                                     selectedGroup = currentGroup;
                                     break;
                                }
                        }
                            int i=0;
                            if(selectedGroup != null){
                                Iterator<MetaContact> itr = group.getChildContacts();
                                while(itr.hasNext()){
                                       i++; 
                                    MetaContact contact = itr.next();
                                    MetaContactListManager.moveMetaContactToGroup(contact, selectedGroup);
                                    XmppConnector.mergingThreads = i;
                                }
                            }
                            init();
                            JOptionPane.showMessageDialog(null, "Merge Completed !!");
                }
            });
            this.moveToMenu.add(menuItem);
        }
    }

    /**
     * Initializes all plugin components.
     */
    private void initPluginComponents()
    {
        // Search for plugin components registered through the OSGI bundle
        // context.
        ServiceReference[] serRefs = null;

        String osgiFilter =
            "(" + Container.CONTAINER_ID + "="
                + Container.CONTAINER_GROUP_RIGHT_BUTTON_MENU.getID() + ")";

        try
        {
            serRefs =
                GuiActivator.bundleContext.getServiceReferences(
                    PluginComponentFactory.class.getName(), osgiFilter);
        }
        catch (InvalidSyntaxException exc)
        {
            logger.error("Could not obtain plugin reference.", exc);
        }

        if (serRefs != null)
        {
            for (int i = 0; i < serRefs.length; i++)
            {
                PluginComponentFactory factory =
                    (PluginComponentFactory) GuiActivator.bundleContext
                        .getService(serRefs[i]);
                PluginComponent component =
                    factory.getPluginComponentInstance(this);

                component.setCurrentContactGroup(group);

                this.add((Component) component.getComponent());

                this.repaint();
            }
        }

        GuiActivator.getUIService().addPluginComponentListener(this);
    }

    /**
     * Handles the <tt>ActionEvent</tt>. The chosen menu item should correspond
     * to an account, where the new contact will be added. We obtain here the
     * protocol provider corresponding to the chosen account and show the
     * dialog, where the user could add the contact.
     *
     * @param e the <tt>ActionEvent</tt> that notified us
     */
    @SuppressWarnings("deprecation")
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem item = (JMenuItem) e.getSource();
        if (item.equals(removeGroupItem))
        {
            if (group != null)
                MetaContactListManager.removeMetaContactGroup(group);
        }
        else if (item.equals(renameGroupItem))
        {

            RenameGroupDialog dialog = new RenameGroupDialog(mainFrame, group);

            dialog.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 200,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 50);

            dialog.setVisible(true);

            dialog.requestFocusInFiled();
        } else if (item.equals(addContactItem))
        {
            AddContactDialog dialog
                = new AddContactDialog(mainFrame);

            dialog.setSelectedGroup(group);

            dialog.setVisible(true);
        }
        else if (item.equals(moveToMenu))
        {
           System.out.println("Hello");
        }
        else if(item.equals(sendBroadcastMessageItem)){
            
            if(!GroupRightButtonMenu.isAlreadyOpened){
                
                GroupRightButtonMenu.isAlreadyOpened = true;
                cs = new CustomSendMessage();
                try{
                    cs.login("ab", "cd");
                }catch(Exception ex){
                    ex.printStackTrace();
                }
               
                    new Thread("Broadcast thread") {
                        @Override
                        public void run() {
                            GroupLoader grp = new GroupLoader();
                            
                            List<RosterGroup> allGroup = new ArrayList<RosterGroup>();
                                allGroup = grp.getAllGroups(group.getGroupName());
                            uiLoader = new BroadcastUI(allGroup,cs,group); 
                        }
                    }.start();
                   
            }else{
               
                new BroadcastUI().toFrontFrame();
            }
            
           
        }
    }

     void sendBroadcastMessage(CustomSendMessage csm ,String message, Set<String> selectedNodes){
        
         Iterator<String> it = selectedNodes.iterator();
         
        while(it.hasNext()){
            try{
                csm.sendMessage(message, it.next().toString());
            }
            catch (Exception e1){
                e1.printStackTrace();
            }               
        } 
       GroupRightButtonMenu.isAlreadyOpened = false;
    }
    
    /**
     * Indicates that a plugin component has been added to this container.
     *
     * @param event the <tt>PluginComponentEvent</tt> that notified us
     */
    /**
     * Indicates that a new plugin component has been added. Adds it to this
     * container if it belongs to it.
     *
     * @param event the <tt>PluginComponentEvent</tt> that notified us
     */
    
    public void pluginComponentAdded(PluginComponentEvent event)
    {
        PluginComponentFactory factory = event.getPluginComponentFactory();

        if (!factory.getContainer().equals(
                Container.CONTAINER_GROUP_RIGHT_BUTTON_MENU))
            return;

        PluginComponent c = factory.getPluginComponentInstance(this);

        this.add((Component) c.getComponent());

        c.setCurrentContactGroup(group);

        this.repaint();
    }

    /**
     * Indicates that a new plugin component has been removed. Removes it to
     * from this container if it belongs to it.
     *
     * @param event the <tt>PluginComponentEvent</tt> that notified us
     */
    public void pluginComponentRemoved(PluginComponentEvent event)
    {
        PluginComponentFactory factory = event.getPluginComponentFactory();

        if (factory.getContainer()
            .equals(Container.CONTAINER_GROUP_RIGHT_BUTTON_MENU))
        {
            this.remove((Component)factory.getPluginComponentInstance(this)
                    .getComponent());
        }
    }

    /**
     * Reloads label icons.
     */
    public void loadSkin()
    {
        addContactItem.setIcon(new ImageIcon(ImageLoader
            .getImage(ImageLoader.ADD_CONTACT_16x16_ICON)));

        removeGroupItem.setIcon(new ImageIcon(ImageLoader
            .getImage(ImageLoader.DELETE_16x16_ICON)));

        renameGroupItem.setIcon(new ImageIcon(ImageLoader
            .getImage(ImageLoader.RENAME_16x16_ICON)));
        
        sendBroadcastMessageItem.setIcon(new ImageIcon(ImageLoader 
            .getImage(ImageLoader.BROADCAT_MESSAGE_16x16_ICON)));
    }
}

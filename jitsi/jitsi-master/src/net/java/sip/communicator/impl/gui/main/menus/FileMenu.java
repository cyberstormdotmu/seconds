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
package net.java.sip.communicator.impl.gui.main.menus;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.java.sip.communicator.impl.gui.GuiActivator;
import net.java.sip.communicator.impl.gui.main.account.NewAccountDialog;
import net.java.sip.communicator.impl.gui.main.chatroomslist.ChatRoomTableDialog;
import net.java.sip.communicator.impl.gui.main.contactlist.AddContactDialog;
import net.java.sip.communicator.impl.gui.main.contactlist.addgroup.CreateGroupDialog;
import net.java.sip.communicator.impl.gui.utils.ImageLoader;
import net.java.sip.communicator.plugin.desktoputil.SIPCommMenu;
import net.java.sip.communicator.service.gui.UIService;
import net.java.sip.communicator.service.gui.XmppConnector;
import net.java.sip.communicator.service.muc.MUCService;
import net.java.sip.communicator.util.ConfigurationUtils;
import net.java.sip.communicator.util.Logger;
import net.java.sip.communicator.util.skin.Skinnable;

import org.jitsi.service.resources.ResourceManagementService;

/**
 * The <tt>FileMenu</tt> is a menu in the main application menu bar that
 * contains "New account".
 *
 * @author Yana Stamcheva
 * @author Lubomir Marinov
 * @author Adam Netocny
 */
public class FileMenu
extends SIPCommMenu
implements  ActionListener,
Skinnable
{
    /**
     * The <tt>Logger</tt> used by the <tt>FileMenu</tt> class and its instances
     * for logging output.
     */
    private static final Logger logger = Logger.getLogger(FileMenu.class);

    private Frame parentWindow;

    /**
     * Add new account menu item.
     */
    private JMenuItem newAccountMenuItem;

    /**
     * Add new contact menu item.
     */
    private JMenuItem addContactItem;

    /**
     * Import Export Contacts.
     */
    private JMenuItem importItem;
    private JMenuItem exportItem;
    /**
     * Create group menu item.
     */
    private JMenuItem createGroupItem;

    /**
     * Chat rooms menu item.
     */
    private JMenuItem myChatRoomsItem;

    /**
     * Close menu item.
     */
    private JMenuItem closeMenuItem;

    /**
     * Indicates if this menu is shown for the chat window or the contact list
     * window.
     */
    private boolean isChatMenu;

    /**
     * Creates an instance of <tt>FileMenu</tt>.
     * @param parentWindow The parent <tt>ChatWindow</tt>.
     */
    private JFrame frame;

    private ImportXml iXML = new ImportXml();

    private ExportFile exp;

    public FileMenu(Frame parentWindow)
    {
        this(parentWindow, false);
    }

    public FileMenu()
    {
        initialize();
    }

    private void initialize()
    {
        frame = new JFrame();  
        frame.setBounds(100, 100, 463, 327);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JSystemFileChooser chooser = new JSystemFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "XML File", "xml");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(frame);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                chooser.getSelectedFile().getName()+">>"+chooser.getSelectedFile().getAbsolutePath());
            try{
                if(chooser.getSelectedFile().getAbsolutePath() != null)
                    iXML.parseXML(chooser.getSelectedFile().getAbsolutePath());
            }catch(Exception e){
                e.printStackTrace();
            }
        }else if(returnVal == JFileChooser.CANCEL_OPTION){
            frame.dispose();
        }

    }

    /**
     * Creates an instance of <tt>FileMenu</tt>.
     *
     * @param parentWindow The parent <tt>ChatWindow</tt>.
     * @param isChatMenu indicates if this menu would be shown for a chat
     * window
     */
    public FileMenu(Frame parentWindow, boolean isChatMenu)
    {
        super(GuiActivator.getResources().getI18NString("service.gui.FILE"));

        this.isChatMenu = isChatMenu;

        ResourceManagementService resources = GuiActivator.getResources();
        createGroupItem = new JMenuItem(
            resources.getI18NString("service.gui.CREATE_GROUP"));
        myChatRoomsItem = new JMenuItem(
            resources.getI18NString("service.gui.MY_CHAT_ROOMS"));

        this.parentWindow = parentWindow;

        // whether the last item added was a separator
        boolean endsWithSeparator = false;

        if (ConfigurationUtils.isShowAccountConfig())
        {

            newAccountMenuItem = new JMenuItem(
                resources.getI18NString("service.gui.NEW_ACCOUNT"));


            this.add(newAccountMenuItem);

            newAccountMenuItem.setName("newAccount");

            newAccountMenuItem.addActionListener(this);

            newAccountMenuItem.setMnemonic(resources
                .getI18nMnemonic("service.gui.NEW_ACCOUNT"));

            // add separator only if there are other items enabled
            if(!ConfigurationUtils.isAddContactDisabled()
                || !ConfigurationUtils.isCreateGroupDisabled()
                || !ConfigurationUtils.isGoToChatroomDisabled())
            {
                this.addSeparator();
                endsWithSeparator = true;
            }
        }

        if (!isChatMenu && !ConfigurationUtils.isAddContactDisabled())
        {
            addContactItem = new JMenuItem(
                resources.getI18NString("service.gui.ADD_CONTACT") + "...");

            this.add(addContactItem);

            addContactItem.setName("addContact");

            addContactItem.addActionListener(this);

            addContactItem.setMnemonic(resources
                .getI18nMnemonic("service.gui.ADD_CONTACT"));

            // if next item is disabled add separator here
            // only if there is something next
            if(ConfigurationUtils.isCreateGroupDisabled()
                && !ConfigurationUtils.isGoToChatroomDisabled())
            {
                this.addSeparator();
                endsWithSeparator = true;
            }
            else
                endsWithSeparator = false;
        }

        if (!isChatMenu && !ConfigurationUtils.isCreateGroupDisabled())
        {
            this.add(createGroupItem);

            // add separator if there is something next
            if(!ConfigurationUtils.isGoToChatroomDisabled())
            {
                this.addSeparator();
                endsWithSeparator = true;
            }
        }

        if (!ConfigurationUtils.isGoToChatroomDisabled()
            && !GuiActivator.getConfigurationService()
            .getBoolean(MUCService.DISABLED_PROPERTY, false))
        {
            this.add(myChatRoomsItem);
            endsWithSeparator = false;
        }

        importItem = new JMenuItem(
            resources.getI18NString("service.gui.IMPORT_CONTACTS"));
        exportItem = new JMenuItem(
            resources.getI18NString("service.gui.EXPORT_CONTACTS"));

        this.add(importItem);
        this.add(exportItem);

        registerCloseMenuItem(!endsWithSeparator);

        // All items are now instantiated and could safely load the skin.
        loadSkin();

        //this.addContactItem.setIcon(new ImageIcon(ImageLoader
        //        .getImage(ImageLoader.ADD_CONTACT_16x16_ICON)));

        createGroupItem.setName("createGroup");
        myChatRoomsItem.setName("myChatRooms");

        createGroupItem.addActionListener(this);
        myChatRoomsItem.addActionListener(this);

        this.setMnemonic(resources
            .getI18nMnemonic("service.gui.FILE"));

        createGroupItem.setMnemonic(resources
            .getI18nMnemonic("service.gui.CREATE_GROUP"));
        myChatRoomsItem.setMnemonic(resources
            .getI18nMnemonic("service.gui.MY_CHAT_ROOMS"));
    }

    /**
     * Loads icons.
     */
    public void loadSkin()
    {
        if (newAccountMenuItem != null)
            newAccountMenuItem.setIcon(
                new ImageIcon(ImageLoader.getImage(
                    ImageLoader.ADD_ACCOUNT_MENU_ICON)));

        if (addContactItem != null)
            addContactItem.setIcon(
                new ImageIcon(ImageLoader.getImage(
                    ImageLoader.ADD_CONTACT_16x16_ICON)));

        createGroupItem.setIcon(
            new ImageIcon(ImageLoader.getImage(
                ImageLoader.GROUPS_16x16_ICON)));
        myChatRoomsItem.setIcon(
            new ImageIcon(ImageLoader.getImage(
                ImageLoader.CHAT_ROOM_MENU_ICON)));

        importItem.setIcon(
            new ImageIcon(ImageLoader.getImage(
                ImageLoader.importIcon)));

        importItem.setName("import");

        exportItem.setIcon(
            new ImageIcon(ImageLoader.getImage(
                ImageLoader.exportIcon)));

        exportItem.setName("export");

        importItem.addActionListener(this);
        exportItem.addActionListener(this);

        if(closeMenuItem != null)
        {
            closeMenuItem.setIcon(
                new ImageIcon(ImageLoader.getImage(
                    ImageLoader.QUIT_16x16_ICON)));
        }
    }

    /**
     * Handles the <tt>ActionEvent</tt> when one of the menu items is selected.
     * @param e the <tt>ActionEvent</tt> that notified us
     */

    private boolean isimport = false;
    private boolean isExport = false;

    public void actionPerformed(ActionEvent e)
    {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        String itemName = menuItem.getName();

        if (itemName.equals("newAccount"))
        {
            NewAccountDialog.showNewAccountDialog();
        }
        else if (itemName.equals("addContact"))
        {
            AddContactDialog dialog = new AddContactDialog(parentWindow);

            dialog.setVisible(true);
        }
        else if (itemName.equals("createGroup"))
        {
            CreateGroupDialog dialog = new CreateGroupDialog(parentWindow);

            dialog.setVisible(true);
        }
        else if (itemName.equals("close"))
        {
            closeActionPerformed();
        }
        else if (itemName.equals("myChatRooms"))
        {
            ChatRoomTableDialog.showChatRoomTableDialog();
        }
        else if(itemName.equals("import"))
        {
            if(!isimport){
                isimport = true;
                if(XmppConnector.xmppUserName == null){
                    JOptionPane.showMessageDialog(null, "Please login to import your contacts ...");
                }else{
                    EventQueue.invokeLater(new Runnable()
                    {

                        @Override
                        public void run()
                        {
                            try{
                                FileMenu fm = new FileMenu();
                                fm.frame.setVisible(true);
                                fm.frame.dispose();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } 
            else
            {
                isimport = false;
            }
        }
        else if(itemName.equals("export"))
        {
            exp = new ExportFile();
            if(!isExport){
                isExport = true;
                new Thread()
                {
                    boolean isCompleted = false;
                    boolean isCancelled = false;
                    String path = null;
                    public void run() {

                        while(!isCompleted ){
                         
                            if(XmppConnector.xmppUserName == null){
                                JOptionPane.showMessageDialog(null, "Please login to export your contacts ...");
                                isCompleted = true;
                            }else{
                                JSystemFileChooser chooser = new JSystemFileChooser();
                                if(!isCancelled){
                                    chooser.setSelectedFile(new File("D:"+File.separator+XmppConnector.xmppUserName+".xml"));
                                }else{
                                    isCancelled = false;
                                    // chooser.setCurrentDirectory(new File(path));
                                       chooser.setSelectedFile(new File(path+File.separator+XmppConnector.xmppUserName+".xml"));  
                                }

                                chooser.setAcceptAllFileFilterUsed(false);
                                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                                    "XML File", "xml");
                                chooser.setFileFilter(filter);

                                int returnVal = chooser.showSaveDialog(frame);
                                if(returnVal == JFileChooser.APPROVE_OPTION) {
                                    path = chooser.getSelectedFile().getAbsolutePath();

                                    if(new File(path).exists()){
                                        System.out.println("file already present");
                                        if(0 == JOptionPane.showConfirmDialog (null, "File already exist. Would you like to overwrite ?","Warning",JOptionPane.YES_NO_OPTION)){
                                            isCompleted = true;    
                                            iXML.exportXML(path);
                                        }else{
                                            isCancelled = true;
                                            isCompleted = false;
                                        }

                                    }else{
                                        isCompleted = true;
                                        iXML.exportXML(path);
                                    }
                                }else{
                                    isCompleted = true;
                                }

                            }

                        }
                    }
                }.start();
            }else{
                isExport = false;
            }

        }
    }

    /**
     * Indicates that the close menu has been selected.
     */
    void closeActionPerformed()
    {
        GuiActivator.getUIService().beginShutdown();
    }

    /**
     * Registers the close menu item.
     * @param addSeparator whether we should add separator before the menu item.
     */
    private void registerCloseMenuItem(boolean addSeparator)
    {
        UIService uiService = GuiActivator.getUIService();
        if ((uiService == null) || !uiService.useMacOSXScreenMenuBar()
            || !registerCloseMenuItemMacOSX())
        {
            registerCloseMenuItemNonMacOSX(addSeparator);
        }
    }

    /**
     * Registers the close menu item for the MacOSX platform.
     * @return <tt>true</tt> if the operation succeeded, <tt>false</tt> -
     * otherwise
     */
    private boolean registerCloseMenuItemMacOSX()
    {
        return registerMenuItemMacOSX("Quit", this);
    }

    /**
     * Registers the close menu item for the MacOSX platform.
     * @param menuItemText the name of the item
     * @param userData the user data
     * @return <tt>true</tt> if the operation succeeded, <tt>false</tt> -
     * otherwise
     */
    static boolean registerMenuItemMacOSX(String menuItemText, Object userData)
    {
        Exception exception = null;
        try
        {
            Class<?> clazz = Class.forName(
                "net.java.sip.communicator.impl.gui.main.menus.MacOSX"
                    + menuItemText + "Registration");
            Method method = clazz.getMethod("run", new Class[]
                { Object.class });
            Object result = method.invoke(null, new Object[]
                { userData });

            if (result instanceof Boolean)
                return (Boolean) result;
        }
        catch (ClassNotFoundException ex)
        {
            exception = ex;
        }
        catch (IllegalAccessException ex)
        {
            exception = ex;
        }
        catch (InvocationTargetException ex)
        {
            exception = ex;
        }
        catch (NoSuchMethodException ex)
        {
            exception = ex;
        }
        if (exception != null)
            logger.error("Failed to register Mac OS X-specific " + menuItemText
                + " handling.", exception);
        return false;
    }

    /**
     * Registers the close menu item for all NON-MacOSX platforms.
     * @param addSeparator whether we should add separator before the menu item.
     */
    private void registerCloseMenuItemNonMacOSX(boolean addSeparator)
    {
        closeMenuItem = new JMenuItem(
            GuiActivator.getResources().getI18NString("service.gui.QUIT"));

        if(addSeparator)
            this.addSeparator();

        if (!isChatMenu)
        {
            this.add(closeMenuItem);
            closeMenuItem.setName("close");
            closeMenuItem.addActionListener(this);
            closeMenuItem.setMnemonic(GuiActivator.getResources()
                .getI18nMnemonic("service.gui.QUIT"));
        }
    }

}

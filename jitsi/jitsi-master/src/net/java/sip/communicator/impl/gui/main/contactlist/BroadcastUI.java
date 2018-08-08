package net.java.sip.communicator.impl.gui.main.contactlist;


import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.java.sip.communicator.impl.gui.utils.ImageLoader;
import net.java.sip.communicator.impl.protocol.jabber.GroupLoader;
import net.java.sip.communicator.service.contactlist.MetaContactGroup;
import net.java.sip.communicator.service.gui.XmppConnector;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;

public class BroadcastUI extends JFrame {

    /*
     * @author TatvaSoft
     */
    
    private static final long serialVersionUID = 1L;
    
    private ImageLoader imageLoader = new ImageLoader() ;
    private JPanel contentPane;
    private JButton btnSend;
    private JTextArea sendMessageField;
    private JCheckBox check;
    private static JFrame sendFrame;
    private GroupRightButtonMenu grb;
    private String tatvaText = "centos.tatva.com";
    
    private List<JCheckBox> checkboxes; 
    
    private JCheckBox groupName;
    private  Box box_1 ;
    Map<Integer,String> groupNameList;
    
    BroadcastUI(){
    }
    
    public BroadcastUI(List<RosterGroup> allGroup,CustomSendMessage csm,MetaContactGroup grp) {
        setResizable(false);
        
        // TatvaSoft
        
        sendFrame = new JFrame();
        sendFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);        
        
        setType(Type.POPUP);
        setTitle("Group Message");
        sendFrame.setUndecorated(true);
        
        setIconImage(ImageLoader.getImage(ImageLoader.SIP_COMMUNICATOR_LOGO));
        sendFrame.setTitle("Broadcast Message Panel");
        sendFrame.setSize(498,467); // default size is 0,0
        setLocation(10,200);
        setBounds(100, 100, 488, 537);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel sendMessagePanel = new JPanel();
        sendMessagePanel.setBackground(new Color(220, 235, 244));
        sendMessagePanel.setForeground(Color.BLUE);
        sendMessagePanel.setBounds(0, 0, 649, 367);
        contentPane.add(sendMessagePanel);
        sendMessagePanel.setLayout(null);

        JLabel lblEnterMessage = new JLabel("--  Enter Broadcast Message  -------------------------------------");
        lblEnterMessage.setHorizontalAlignment(SwingConstants.LEFT);
        lblEnterMessage.setFont(new Font("Calibri", Font.BOLD, 13));
        lblEnterMessage.setForeground(Color.BLACK);
        lblEnterMessage.setBounds(38, 35, 312, 34);
        sendMessagePanel.add(lblEnterMessage);
  
        
        JScrollPane messagePane = new JScrollPane();
        
        messagePane.setBounds(38, 69, 312, 218);
        messagePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sendMessagePanel.add(messagePane);
        
                sendMessageField = new JTextArea();
                messagePane.setViewportView(sendMessageField);
                sendMessageField.setToolTipText("Broadcast Message Here");
                sendMessageField.setLineWrap(true);
                
        btnSend = new JButton("Send");
        btnSend.setBackground(new Color(240, 255, 240));
        btnSend.setEnabled(false);
        btnSend.setToolTipText("Send Broadcast Message");
        btnSend.setForeground(Color.BLACK);
        
        btnSend.setBounds(48, 308, 146, 23);
        sendMessagePanel.add(btnSend);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setToolTipText("Close Frame");
        btnCancel.setForeground(Color.BLACK);

        btnCancel.setBounds(204, 308, 146, 23);
        sendMessagePanel.add(btnCancel);

        JLabel lblNewLabel = new JLabel();
        lblNewLabel.setBounds(86, 35, 146, 14);
        sendMessagePanel.add(lblNewLabel);
        
        checkboxes = new ArrayList<JCheckBox>();
        
        // Scrollable panel for contacts of Group
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(372, 69, 256, 280);
        scrollPane.setBackground(new Color(255, 255, 255));
        sendMessagePanel.add(scrollPane);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        
        box_1 = Box.createVerticalBox();
        scrollPane.setViewportView(box_1);
        box_1.setBackground(new Color(255, 255, 255));
        box_1.setOpaque(true);
        box_1.setBackground(Color.white);
        
        final Insets checkBoxMargin = new Insets(0, 13, 1, 0);
        
        groupNameList = new HashMap<Integer,String>();
        
        Integer groupSize = allGroup.size();
        
        for(int i=0;i<groupSize;i++){
            
            groupName = new JCheckBox(allGroup.get(i).getName());
            groupName.setName(Integer.toString(i));
            groupNameList.put(i, allGroup.get(i).getName());
            groupName.setVerticalAlignment(SwingConstants.BOTTOM);
            groupName.setFont(new Font("Tahoma", Font.BOLD, 12));
            groupName.setHorizontalAlignment(SwingConstants.LEFT);
            groupName.setForeground(Color.BLACK);
            groupName.setBackground(Color.white);
            box_1.add(groupName);
            
            groupName.addItemListener(new ItemListener()
            {
                
                @Override
                public void itemStateChanged(ItemEvent e){
                    // TODO Auto-generated method stub
                    stateChanged(e);
                    
                }

            });
            
            RosterGroup currentGroup = allGroup.get(i);
            List<RosterEntry> currentGroupsContacts = (List<RosterEntry>) currentGroup.getEntries();
            
            for(int j=0;j<currentGroup.getEntries().size();j++){
                check = new JCheckBox(currentGroupsContacts.get(j).getUser().toString());
                check.setFont(getFontByStatus(currentGroupsContacts.get(j).getUser().toString()));
                check.setForeground(Color.black );
                check.setName(Integer.toString(i));
                check.setMargin(checkBoxMargin);
                
                sendMessagePanel.add(check);
                check.setBackground(Color.white);
                checkboxes.add(check);
                box_1.add(check);
                
                check.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {

                        JCheckBox child = (JCheckBox) e.getSource();
                        String parentText = findParent(child);
                        
                    }
                });
                
            };
        }
        
        Component[] cp = box_1.getComponents();
        boolean isFound = false;
        for(Component childComponent : cp){
            if(childComponent instanceof JCheckBox && !isFound){
                JCheckBox childBox = (JCheckBox) childComponent;
                    if(childBox.getText().equals(grp.getGroupName())){
                        childBox.setSelected(true);
                        isFound = true;
                    }
            }
        }
        isFound = false;
        
        //Box consists list of all group user 
        
        JLabel lblNewLabel_1 = new JLabel("Choose Person(s)");
        lblNewLabel_1.setFont(new Font("Calibri", Font.BOLD, 13));
        lblNewLabel_1.setBounds(372, 39, 105, 14);
        sendMessagePanel.add(lblNewLabel_1);
        
        sendFrame = this;
        sendFrame.setVisible(true);
        
        
        this.setSize(655, 394);
        
        // Listeners 
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        
        btnSend.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Set<String> selectedNodes = new HashSet<String>();
                    for(JCheckBox box : checkboxes){
                        if(box.isSelected() && !box.getText().contains(XmppConnector.xmppUserName) ){
                            selectedNodes.add(box.getText());
                        }
                    }
                    String message = sendMessageField.getText();
                    if(!message.trim().isEmpty() && !selectedNodes.isEmpty()){
                         grb = new GroupRightButtonMenu();
                         grb.sendBroadcastMessage(csm, message, selectedNodes);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                sendFrame.dispose();
                GroupRightButtonMenu.isAlreadyOpened = false;
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setBackground(new Color(51, 204, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton)e.getSource();
                button.setBackground(new Color(240, 255, 240));
            }
        });

        btnCancel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                sendFrame.dispose();
                GroupRightButtonMenu.isAlreadyOpened = false;
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(new Color(51, 204, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(new Color(240, 255, 240));
            }
        });
        
        sendMessageField.addKeyListener(new KeyAdapter() {
            @Override
            
            public void keyReleased(KeyEvent e) {
                
                String message = sendMessageField.getText();
                if(message.trim().isEmpty()){
                    btnSend.setEnabled(false);
                }else{                
                    btnSend.setEnabled(true);
                }
            }
        });
        
        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                e.getWindow().dispose();
                GroupRightButtonMenu.isAlreadyOpened = false;
            }
        });
        
    }
    
    private String findParent(JCheckBox child){
        Integer groupValue = Integer.parseInt(child.getName());
        String groupName = null;
            if(groupNameList != null){
                 groupName = groupNameList.get(groupValue);
            }
        return groupName;
    }
    
    private void stateChanged(ItemEvent e){
        JCheckBox parent = (JCheckBox) e.getSource();
        String parentObjKey = parent.getName();
        Component[] cmp = box_1.getComponents();
        if(e.getStateChange() == ItemEvent.SELECTED){
            for(Component childComponent : cmp){
                if(childComponent instanceof JCheckBox){
                    JCheckBox childBox = (JCheckBox) childComponent;
                        if(childBox.getName().equals(parentObjKey) && childBox.getText().contains(tatvaText)){
                            childBox.setSelected(true);
                        }
                }
            }
        }else if(e.getStateChange() == ItemEvent.DESELECTED){
            for(Component childComponent : cmp){
                if(childComponent instanceof JCheckBox){
                    JCheckBox childBox = (JCheckBox) childComponent;
                        if(childBox.getName().equals(parentObjKey) && childBox.getText().contains(tatvaText)){
                            childBox.setSelected(false);
                        }
                }
            }
        }
    }
    
    GroupLoader statusFetcher = new GroupLoader();
    private Font getFontByStatus(String user){
        return statusFetcher.getStatus(user);
    }
    
    void toFrontFrame(){
        if(BroadcastUI.sendFrame != null){
            new Thread(){
                public void run(){
                    try
                    {
                        Thread.sleep(200);
                            if(BroadcastUI.sendFrame.getState() == Frame.ICONIFIED){
                                System.out.println("Opening Minimized Window ...");
                                BroadcastUI.sendFrame.setState(Frame.NORMAL);
                                BroadcastUI.sendFrame.setVisible(true); 
                            }
                        BroadcastUI.sendFrame.toFront();
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    /*BroadcastUI.sendFrame.setAlwaysOnTop(true);
                    BroadcastUI.sendFrame.setAlwaysOnTop(false);*/
                }
            }.start();
            
        }
    }
}   

package com.company.views;

import com.company.ChatType;
import com.company.connection.ClientSocket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ChatFrame extends JFrame{
    MsgPanel pChat;
    JTextField tMsg;
    JLabel lCurrentChat;
    JButton bSend;
    JTabbedPane tpControl;
    GroupLayout groupLayout;
    String user;
    public ClientSocket socket;

    public ChatFrame(ClientSocket socket, String user){
        this.user = user;
        this.socket = socket;
        this.setTitle("CocoChat");
        this.getContentPane().setBackground(Color.lightGray);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(900,676));
        this.setPreferredSize(new Dimension(1200, 700));
        this.revalidate();
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        groupLayout = new GroupLayout(this.getContentPane());
        componentConfig();
        tpConfig();
        glConfig();
    }

    private void tpConfig(){
        FriendsPanel friendsPanel = new FriendsPanel();
        tpControl.add("Amigos", new JScrollPane(new FriendsPanel()));
        GroupsPanel groupsPanel = new GroupsPanel();
        tpControl.add("Grupos", new JScrollPane(new ListPanel(this, ChatType.groups, socket)));
        UsersPanel usersPanel = new UsersPanel();
        tpControl.add("Usuarios", usersPanel);
        SettingsPanel settingsPanel = new SettingsPanel(this, user, ChatType.Friend);
        tpControl.add("Settings", settingsPanel);
    }

    private  void componentConfig(){
        pChat = new MsgPanel();
        tMsg = new JTextField("tMsg");
        lCurrentChat = new JLabel("CurrentChat");
        bSend = new JButton("send");
        tpControl = new JTabbedPane();
    }

    private void glConfig(){
        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup()
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(lCurrentChat, 50, 50,100)
                    .addComponent(pChat, 200, 550, 700)
                    .addGroup(groupLayout.createParallelGroup()
                        .addComponent(tMsg, 75, 75, 100)
                        .addComponent(bSend, 75, 75, 100)
                    )
                )
                .addComponent(tpControl, 576, 700, 1080)
        );

        groupLayout.setHorizontalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                    .addComponent(lCurrentChat, 100, 300, 500)
                    .addComponent(pChat, 300, 750, 1050)
                    .addGroup(groupLayout.createSequentialGroup()
                        .addComponent(tMsg, 375, 650, 900)
                        .addComponent(bSend, 75, 100, 150)
                    )
                )
                .addComponent(tpControl, 200, 400, 700)
        );

        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setLayout(groupLayout);
        this.pack();
    }
}

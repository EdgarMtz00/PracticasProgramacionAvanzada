package com.company.views;

import com.company.ChatType;
import com.company.connection.ClientSocket;
import com.company.connection.EventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


/**
 *Chat Frame
 * ventana principal y control para enviar mensajes
 */
public class ChatFrame extends JFrame {
    MsgPanel pChat;
    JTextField tMsg;
    JLabel lCurrentChat;
    ChatType currentType = ChatType.None;
    JButton bSend;
    JTabbedPane tpControl;
    GroupLayout groupLayout;
    ListPanel friendsTab, groupsTab, usersTab, friendRequestTabs;
    String user, username;
    JsonObject data;
    JScrollPane jScrollPane;

    public ClientSocket socket;

    public ChatFrame(ClientSocket socket, String user, String username) {
        this.user = user;
        this.username = username;
        this.socket = socket;
        this.setTitle("CocoChat");
        this.getContentPane().setBackground(Color.lightGray);
        this.setVisible(true);
        this.setMinimumSize(new Dimension(900, 676));
        this.setPreferredSize(new Dimension(1200, 700));
        this.revalidate();
        this.setResizable(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        groupLayout = new GroupLayout(this.getContentPane());
        componentConfig();
        tpConfig();
        glConfig();

        socket.addEventListener(new EventListener() {
            @Override
            public boolean shouldReceiveMessage(JsonObject response) {
                return response.get("tipo").getAsString().equals("solicitud");
            }

            @Override
            public void execute(JsonObject response) {
                JOptionPane.showMessageDialog(null, response.get("mensaje").getAsString());
            }
        });
    }

    /**
     * Configura tpControl y sus componentes
     */
    private void tpConfig() {
        tpControl.add("Amigos", new JScrollPane(friendsTab));
        tpControl.add("Grupos", new JScrollPane(groupsTab));
        tpControl.add("Usuarios", new JScrollPane(usersTab));
        tpControl.add("Solicitudes", new JScrollPane(friendRequestTabs));

        SettingsPanel settingsPanel = new SettingsPanel(this, user, ChatType.None, username);
        tpControl.add("Settings", settingsPanel);
        friendsTab.focusPanel();
        tpControl.addChangeListener(changeEvent -> {
            JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            switch (index) {
                case 0:
                    friendsTab.focusPanel();
                    break;
                case 1:
                    groupsTab.focusPanel();
                    break;
                case 2:
                    usersTab.focusPanel();
                    break;
                case 3:
                    friendRequestTabs.focusPanel();
                    break;
                case 4:
                    settingsPanel.setType(currentType);
                default:
                    break;
            }
        });
    }

    /**
     * Configura los componentes dentro de chatframe
     */
    private void componentConfig() {
        pChat = new MsgPanel(Integer.valueOf(user), socket, this);
        tMsg = new JTextField("");
        lCurrentChat = new JLabel("CurrentChat");
        bSend = new JButton("send");
        tpControl = new JTabbedPane();
        friendsTab = new ListPanel(this, ChatType.friends, socket);
        groupsTab = new ListPanel(this, ChatType.groups, socket);
        usersTab = new ListPanel(this, ChatType.users, socket);
        friendRequestTabs = new ListPanel(this, ChatType.friendRequests, socket);
        jScrollPane = new JScrollPane(pChat);

        bSend.addActionListener(e -> {
            if(!tMsg.getText().equals("")) {
                String message = tMsg.getText();
                tMsg.setText("");
                data = new JsonObject();
                data.addProperty("message", message);
                if (currentType == ChatType.groups) {
                    data.addProperty("group", lCurrentChat.getText());
                    socket.request("groups/sendMessage", data, this::sendMessage);
                } else if (currentType == ChatType.users) {
                    data.addProperty("user", lCurrentChat.getText());
                    socket.request("users/sendMessage", data, this::sendMessage);
                } else if (currentType != ChatType.friendRequests) {
                    data.addProperty("friend", lCurrentChat.getText());
                    socket.request("friends/sendMessage", data, this::sendMessage);
                }
            }
        });
    }


    /**
     *Listener para cuando se envia un mensaje
     * @param response respuesta del servidor
     */
    private void sendMessage(JsonObject response) {
        if (response.get("status").getAsString().equals("ok")) {
            pChat.setMessages(username + ": " + data.get("message").getAsString());
        }
    }

    /**
     * Configura el grouplayout
     */
    private void glConfig() {
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                                .addComponent(lCurrentChat, 50, 50, 100)
                                .addComponent(jScrollPane, 200, 550, 700)
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
                                .addComponent(jScrollPane, 300, 750, 1050)
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

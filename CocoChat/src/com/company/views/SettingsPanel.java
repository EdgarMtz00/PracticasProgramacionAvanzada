package com.company.views;

import com.company.ChatType;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JCheckBox darkMode;
    private JSeparator jSeparator = new JSeparator();
    private JLabel userLabel, chatLabel;
    private JButton exitBtn, deleteChat, addToChat;
    private ChatFrame parent;
    private GroupLayout groupLayout = new GroupLayout(this);
    private ChatType type;

    public SettingsPanel(ChatFrame parent, String user, ChatType type){
        componentInit();
        this.type = type;
        jSeparator.setPreferredSize(new Dimension(5,  5));
        jSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        viewConfig();
        this.parent = parent;
        userLabel = new JLabel(user);
        exitBtn.addActionListener(e ->{
            new SignFrame(parent.socket);
            parent.dispose();
        });
        glConfig();
    }

    private void viewConfig(){
        switch (type){
            case User:
                addToChat.setText("Enviar solicitud de amistad");
                addToChat.addActionListener(e ->{
                    System.out.println("Solicitud Enviada");
                });
                deleteChat.setVisible(false);
                addToChat.setVisible(true);
                break;
            case Friend:
                addToChat.setText("Crear grupo");
                deleteChat.setText("Eliminar amigo");
                deleteChat.setVisible(true);
                addToChat.setVisible(true);
                addToChat.addActionListener(e ->{
                    System.out.println("Grupo Creado");;
                });
                deleteChat.addActionListener(e ->{
                    System.out.println("Amigo eliminado");
                });
                break;
            case groups:
                addToChat.setText("Añadir amigo");
                deleteChat.setText("Salir del grupo");
                addToChat.setVisible(true);
                deleteChat.setVisible(true);
                addToChat.addActionListener(e -> {
                    System.out.println("Amigo añadido");
                });
                deleteChat.addActionListener(e -> {
                    System.out.println("Te has salido del grupo");
                });
                break;
            case None:
                chatLabel.setVisible(false);
                addToChat.setVisible(false);
                deleteChat.setVisible(false);
                break;
        }
    }

    private void componentInit(){
        chatLabel = new JLabel("friend");
        exitBtn = new JButton("Cerrar Sesion");
        darkMode = new JCheckBox("Modo Oscuro");
        addToChat = new JButton("añadir");
        deleteChat = new JButton("Eliminar");
    }

    private void glConfig(){
        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addComponent(userLabel)
                .addGroup(groupLayout.createParallelGroup()
                    .addComponent(darkMode)
                    .addComponent(exitBtn)
                )
                .addComponent(jSeparator)
                .addComponent(chatLabel)
                .addComponent(addToChat)
                .addComponent(deleteChat)
        );

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup()
                .addComponent(userLabel)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(darkMode)
                    .addComponent(exitBtn)
                )
                .addComponent(jSeparator)
                .addComponent(chatLabel)
                .addComponent(addToChat)
                .addComponent(deleteChat)
        );
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);
        this.setLayout(groupLayout);
    }
}

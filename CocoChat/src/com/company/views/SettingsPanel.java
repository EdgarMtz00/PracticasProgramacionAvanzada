package com.company.views;

import com.company.ChatType;
import com.company.connection.ResponseListener;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

/**
 * pestaña de configuracion
 */
public class SettingsPanel extends JPanel {
    private JSeparator jSeparator = new JSeparator();
    private JLabel userLabel, chatLabel;
    private JButton exitBtn, deleteChat, addToChat;
    private ChatFrame parent;
    private GroupLayout groupLayout = new GroupLayout(this);
    private ChatType type;


    public SettingsPanel(ChatFrame parent, String user, ChatType type, String username) {
        componentInit();
        this.type = type;
        jSeparator.setPreferredSize(new Dimension(5, 5));
        jSeparator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        this.parent = parent;
        userLabel = new JLabel(username);
        deleteChat.addActionListener(e -> {
            System.out.println("hola");
        });
        addToChat.addActionListener(e -> {
            System.out.println("hola");
        });
        exitBtn.addActionListener(e -> {
            parent.socket.request("logout", null, this::logOut);
        });
        viewConfig();
        glConfig();
    }

    /**
     * obtiene el tipo de usuario seleccionado
     * @param type
     */
    public void setType(ChatType type) {
        this.type = type;
        if (type != ChatType.friendRequests)
            viewConfig();
    }

    /**
     * peticion para cerrar sesion
     * @param res
     */
    private void logOut(JsonObject res) {
        if (res.get("status").getAsString().equals("ok")) {
            new SignFrame(parent.socket);
            parent.dispose();
        }
    }

    /**
     * Configura la pestaña de acuerdo al usuario seleccionado
     */
    private void viewConfig() {
        if (deleteChat.getActionListeners().length > 0)
            deleteChat.removeActionListener(deleteChat.getActionListeners()[0]);
        if (addToChat.getActionListeners().length > 0)
            addToChat.removeActionListener(addToChat.getActionListeners()[0]);
        switch (type) {
            case users:
                addToChat.setText("Enviar solicitud de amistad");
                addToChat.addActionListener(e -> {
                    JsonObject req = new JsonObject();
                    req.addProperty("user", parent.lCurrentChat.getText());
                    parent.socket.request("friends/add", req, response -> {
                        if (response.get("status").getAsString().equals("ok"))
                            JOptionPane.showMessageDialog(null, "Solicitud Enviada");
                        else
                            JOptionPane.showMessageDialog(null, "No se puede enviar solicitud");
                    });
                });
                deleteChat.setVisible(false);
                addToChat.setVisible(true);
                break;
            case friends:
                addToChat.setText("Crear grupo");
                deleteChat.setText("Eliminar amigo");
                deleteChat.setVisible(true);
                addToChat.setVisible(true);
                addToChat.addActionListener(e -> {
                    String nombre = JOptionPane.showInputDialog(null, "Nombre del grupo", "");
                    if (!nombre.trim().isEmpty()) {
                        JsonObject req = new JsonObject();
                        req.addProperty("nombre", nombre);
                        req.addProperty("friend", parent.lCurrentChat.getText());
                        parent.socket.request("groups/create", req, response -> {
                            if (response.get("status").getAsString().equals("ok"))
                                JOptionPane.showMessageDialog(null, "Grupo creado");
                            else
                                JOptionPane.showMessageDialog(null, "No se pudo crear el grupo");
                        });
                    }
                    System.out.println(nombre);
                });
                deleteChat.addActionListener(e -> {
                    JsonObject req = new JsonObject();
                    req.addProperty("friend", parent.lCurrentChat.getText());
                    parent.socket.request("friends/delete", req, response -> {
                        if (response.get("status").getAsString().equals("ok"))
                            JOptionPane.showMessageDialog(null, "Amigo eliminado");
                        else
                            JOptionPane.showMessageDialog(null, "No se puede eliminar");
                    });
                });
                break;
            case groups:
                addToChat.setText("Añadir amigo");
                deleteChat.setText("Salir del grupo");
                addToChat.setVisible(true);
                deleteChat.setVisible(true);
                addToChat.addActionListener(e -> {
                    JsonObject req = new JsonObject();
                    req.addProperty("group", parent.lCurrentChat.getText());
                    String user = JOptionPane.showInputDialog(null, "Nombre del grupo", "");
                    if (!user.trim().isEmpty()) {
                        req.addProperty("friend", user);
                        parent.socket.request("groups/add", req, response -> {
                            if (response.get("status").getAsString().equals("ok"))
                                JOptionPane.showMessageDialog(null, "Amigo añadido");
                            else
                                JOptionPane.showMessageDialog(null, "No se ha encontrado el usuario");
                        });
                    }
                });
                deleteChat.addActionListener(e -> {
                    JsonObject req = new JsonObject();
                    req.addProperty("group", parent.lCurrentChat.getText());
                    parent.socket.request("groups/leave", req, response -> {
                        if (response.get("status").getAsString().equals("ok"))
                            JOptionPane.showMessageDialog(null, "Haz salido del grupo");
                        else
                            JOptionPane.showMessageDialog(null, "No se ha podido salir");
                    });
                });
                break;
            case None:
                chatLabel.setVisible(false);
                addToChat.setVisible(false);
                deleteChat.setVisible(false);
                break;
        }
    }

    private void componentInit() {
        chatLabel = new JLabel("friend");
        exitBtn = new JButton("Cerrar Sesion");
        addToChat = new JButton("Añadir");
        deleteChat = new JButton("Eliminar");
    }

    /**
     * Configura el grouplayout
     */
    private void glConfig() {
        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(userLabel)
                        .addGroup(groupLayout.createParallelGroup()
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

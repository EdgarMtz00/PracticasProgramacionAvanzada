package com.company.views;

import com.company.ChatType;
import com.company.connection.ClientSocket;
import com.company.connection.ResponseListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *Lista de usuarios y grupos
 */
public class ListPanel extends JPanel {
    ChatFrame parent;
    ChatType type;
    ClientSocket socket;
    int x = 50;
    int y = 50;

    public ListPanel(ChatFrame parent, ChatType type, ClientSocket socket) {
        this.parent = parent;
        this.type = type;
        this.socket = socket;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setVisible(true);
    }

    /**
     * Metodo invocado cuando se cambia de pestaña
     */
    public void focusPanel() {
        x = 50;
        y = 50;
        removeAll();
        switch (type) {
            case groups:
                socket.request(String.valueOf(type), null, this::fill);
                break;
            case friendRequests:
                socket.request("friends/requests", null, this::fill);
                break;
            default:
                socket.request(type + "/connected", null, this::fill);
                socket.request(String.valueOf(type), null, this::fill);
                break;
        }
    }

    /**
     * Se encarga de llenar la lista y  asignar las acciones de los jlabel
     * cuando recibe una respuesta del servidor
     * @param response
     */
    private void fill(JsonObject response) {
        if (response.get("status").getAsString().equals("ok")) {
            JsonArray data = response.get("data").getAsJsonArray();
            for (int i = 0; i < data.size(); i++) {
                String group = data.get(i).getAsString();
                JLabel jlabel = new JLabel(group);
                if(response.has("connected"))
                    jlabel.setForeground(Color.GREEN);

                jlabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        JLabel o = (JLabel) e.getSource();
                        if (type != ChatType.friendRequests) {

                            if (type == ChatType.users) {
                                JsonObject req = new JsonObject();
                                req.addProperty("user", o.getText());
                                socket.request("users/request", req, response12 -> {
                                    if (response12.get("status") == null) {
                                        return;
                                    }
                                    String status = response12.get("status").getAsString();
                                    if (!response12.get("status").getAsString().equals("ok")) {
                                        JOptionPane.showMessageDialog(null, "No esta conectado, no revisará tus mensajes");
                                    }
                                    parent.lCurrentChat.setText(o.getText());
                                    parent.currentType = type;
                                    parent.pChat.removeAll();
                                    parent.pChat.validate();
                                    parent.pChat.repaint();
                                    parent.pChat.getMessages(o.getText(), type);
                                });
                                return;
                            }

                            parent.lCurrentChat.setText(o.getText());
                            parent.currentType = type;
                            parent.pChat.removeAll();
                            parent.pChat.validate();
                            parent.pChat.repaint();
                            parent.pChat.getMessages(o.getText(), type);
                        } else {
                            int dialogButton = JOptionPane.YES_NO_OPTION;
                            int dialogResult = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres agregarlo como amigo?", "Hola", dialogButton);
                            if (dialogResult == JOptionPane.YES_OPTION) {
                                JsonObject request = new JsonObject();
                                request.addProperty("user", o.getText());
                                socket.request("friends/accept", request, response1 ->
                                        JOptionPane.showMessageDialog(null, "Usuario agregado como amigo :)"));
                            } else {
                                JsonObject request = new JsonObject();
                                request.addProperty("user", o.getText());
                                socket.request("friends/decline", request, response1 ->
                                        JOptionPane.showMessageDialog(null, "La solicitud fue rechazada"));
                            }
                        }
                    }
                });
                this.add(jlabel);
            }
            this.revalidate();
        }
    }
}
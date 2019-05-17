package com.company.views;

import com.company.ChatType;
import com.company.connection.ClientSocket;
import com.company.connection.EventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;

/**
 * Cuadro donde se visualizan los mensajes
 */
class MsgPanel extends JPanel {
    int user;
    ChatFrame parent;
    ClientSocket socket;
    EventListener listener;

    MsgPanel(int user, ClientSocket socket, ChatFrame parent) {
        this.parent = parent;
        this.socket = socket;
        this.user = user;
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        this.setVisible(true);
    }

    /**
     *Peticion de mensajes
     * @param chat usuario o grupo con el que se
     * @param type
     */
    public void getMessages(String chat, ChatType type) {
        if (listener != null) {
            socket.removeEventListener(listener);
        }

        if (type == ChatType.users) {
            listener = new EventListener() {
                @Override
                public boolean shouldReceiveMessage(JsonObject response) {
                    return response.get("tipo").getAsString().equals("mensaje") && response.get("remitente").getAsString().equals(parent.lCurrentChat.getText());
                }

                @Override
                public void execute(JsonObject response) {
                    String sender = response.get("remitente").getAsString();
                    JLabel label = new JLabel(sender + ": " + response.get("mensaje").getAsString());
                    label.setVisible(true);
                    MsgPanel.this.add(label);
                    MsgPanel.this.validate();
                    MsgPanel.this.repaint();
                }
            };
            socket.addEventListener(listener);
        }else {
            JsonObject data = new JsonObject();
            if (type == ChatType.groups) {
                data.addProperty("group", chat);
                socket.request("chats", data, this::setMessages);
                listener = new EventListener() {
                    @Override
                    public boolean shouldReceiveMessage(JsonObject response) {
                        return response.get("tipo").getAsString().equals("mensaje");
                    }

                    @Override
                    public void execute(JsonObject response) {
                        JLabel label = new JLabel(response.get("remitente").getAsString() + ": " + response.get("mensaje").getAsString());
                        label.setVisible(true);
                        MsgPanel.this.add(label);
                        MsgPanel.this.revalidate();
                        MsgPanel.this.validate();
                        MsgPanel.this.repaint();
                    }
                };
                socket.addEventListener(listener);
            } else {
                data.addProperty("friend", chat);
                socket.request("chats", data, this::setMessages);

                listener = new EventListener() {
                    @Override
                    public boolean shouldReceiveMessage(JsonObject response) {
                        return response.get("tipo").getAsString().equals("mensaje") && response.get("remitente").getAsString().equals(parent.lCurrentChat.getText());
                    }

                    @Override
                    public void execute(JsonObject response) {
                        JLabel label = new JLabel(response.get("remitente").getAsString() + ": " + response.get("mensaje").getAsString());
                        label.setVisible(true);
                        MsgPanel.this.add(label);
                        MsgPanel.this.revalidate();
                        MsgPanel.this.validate();
                        MsgPanel.this.repaint();
                    }
                };
                socket.addEventListener(listener);
            }
        }

    }

    public void setMessages(JsonElement response) {
        JsonObject msgData = response.getAsJsonObject();
        if (msgData.get("status").getAsString().equals("ok")) {
            JsonArray messages = msgData.get("data").getAsJsonArray();
            for (int i = 0; i < messages.size(); i++) {
                JsonObject m = messages.get(i).getAsJsonObject();
                String text = m.get("user").getAsString() + ": " + m.get("message").getAsString();
                JLabel jlabel = new JLabel(text);
                this.add(jlabel);
            }
            this.validate();
            this.repaint();
        }
    }

    public void setMessages(String m) {
        JLabel jlabel = new JLabel(m);
        this.add(jlabel);
        this.revalidate();
    }
}
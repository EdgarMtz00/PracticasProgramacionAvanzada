package com.company.views;

import com.company.ChatType;
import com.company.connection.ClientSocket;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListPanel extends JPanel implements ActionListener {
    ChatFrame parent;
    ChatType type;
    public ListPanel(ChatFrame parent, ChatType type, ClientSocket socket){
        this.parent = parent;
        this.type = type;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        socket.setListener(this::fill);
        socket.request(String.valueOf(type), null);

        this.setVisible(true);
    }

    public void fill(JsonObject response){
        JsonArray data =response.get("data").getAsJsonArray();
        int x = 50;
        int y = 50;
        for (int i = 0; i < data.size(); i++){
            String group = data.get(i).getAsString();
            JLabel jlabel = new JLabel(group);
            jlabel.setBounds(x, y, x, y * i);
            this.add(jlabel);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){

    }
}
package com.company;

import com.company.connection.ClientSocket;
import com.company.views.ChatFrame;
import com.company.views.SignFrame;

public class Main {

    public static void main(String[] args) {
	// write your code here.
        ClientSocket socket = new ClientSocket();
        socket.request("", null);
        SignFrame signFrame = new SignFrame(socket);
    }
}

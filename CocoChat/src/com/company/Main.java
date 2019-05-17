package com.company;

import com.company.connection.ClientSocket;
import com.company.views.SignFrame;

public class Main {

    public static void main(String[] args) {
	// write your code here.
        ClientSocket socket = new ClientSocket();
        SignFrame signFrame = new SignFrame(socket);
    }
}

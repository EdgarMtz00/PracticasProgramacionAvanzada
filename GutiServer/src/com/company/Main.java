package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ServerSocket server = new  ServerSocket(5000);
            while(true){
                Socket puto1 = server.accept();
                String Smsg = "eyuyel";
                byte[] msg = new byte[puto1.getInputStream().available()];
                puto1.getInputStream().read(msg);
                System.out.println(puto1.getInetAddress() + ": " + new String(msg, StandardCharsets.UTF_8));
                puto1.close();
                Socket puto2 = server.accept();
                puto2.getOutputStream().write(msg);
                System.out.println(puto2.getInetAddress());
                puto2.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

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
            ServerSocket gutiServer = new  ServerSocket(5000);
            String msgEnviado = "eyuyel esta bien";
            while(true){
                Socket gutiSocket = gutiServer.accept();
                byte[] msgRecibido = new byte[500];
                gutiSocket.getOutputStream().write(msgEnviado.getBytes(StandardCharsets.UTF_8));
                System.out.println("mensaje enviado");
                Thread.sleep(1000);
                gutiSocket.getInputStream().read(msgRecibido);
                msgEnviado = new String(msgRecibido, StandardCharsets.UTF_8);
                System.out.println("'" + gutiSocket.getInetAddress() + "' envia: " + msgEnviado);
                Thread.sleep(1000);
                gutiSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.company;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Main {

    public static void main(String[] args) {
	// write your code here
        try {
            Socket gutiServer = new Socket("127.0.0.1", 5000);
            String msgEnviado = "asdfg";
            byte[] msgRecibido = new byte[500];
            gutiServer.getInputStream().read(msgRecibido);
            System.out.println(new String(msgRecibido, StandardCharsets.UTF_8));
            Thread.sleep(1000);
            gutiServer.getOutputStream().write(msgEnviado.getBytes(StandardCharsets.UTF_8));
            System.out.println("mensje enviado");
            Thread.sleep(1000);
            gutiServer.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.company;

import com.company.tasks.Task;
import com.company.tasks.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class Server {
    private ServerSocket socketServer;
    private final List<Socket> connectedClients;
    private Logger serverLogger;

    Server(int port) throws IOException {
        connectedClients = new ArrayList<>();
        socketServer = new ServerSocket(port);
        serverLogger = Logger.getLogger("Server");
        TaskManager.enqueue(this::waitForData);
        TaskManager.enqueue(this::verifyConnection);

    }

    private void verifyConnection() {
        synchronized (connectedClients) {
            List<Socket> clientsToRemove = new ArrayList<>();
            connectedClients.forEach(clientSocket -> {
                try {
                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(0);
                    outputStream.flush();
                } catch (IOException e) {
                    clientsToRemove.add(clientSocket);
                    serverLogger.info("Se desconecto: " + clientSocket.getInetAddress());
                }
            });
            connectedClients.removeAll(clientsToRemove);
            connectedClients.notify();
        }
        try {
            Thread.sleep(100);
            TaskManager.enqueue(this::verifyConnection);
        } catch (InterruptedException e) {
        }
    }

    private void listenForClients() {
        while (true) {
            try {
                Socket newSocket = socketServer.accept();
                synchronized (connectedClients) {
                    connectedClients.add(newSocket);
                    connectedClients.notify();
                }

                serverLogger.info("Connected client with ip: " + newSocket.getInetAddress());
            } catch (IOException e) {
                serverLogger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    void runForever() {
        serverLogger.info("Se inició el socket");
        listenForClients();
    }

    private void waitForData() {
        while (true) {
            synchronized (connectedClients) {
                connectedClients.forEach(client -> {
                    try {
                        InputStream stream = client.getInputStream();
                        if (stream.available() != 0) {
                            byte[] b = new byte[stream.available()];
                            stream.read(b);
                            serverLogger.info(new String(b, StandardCharsets.UTF_8));
                        }
                    }
                    catch(IOException e) {
                        serverLogger.log(Level.SEVERE, e.getMessage());
                    }
                });
                connectedClients.notify();
            }
        }
    }
}

package com.company;

import com.company.controllers.Controller;
import com.company.tasks.TaskManager;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que gestiona todo lo del servidor
 */
class Server {
    private ServerSocket socketServer;
    private final HashMap<Integer, Socket> loggedClients;
    private final List<Socket> connectedClients;
    private Logger serverLogger;
    private final static Map<String, Method> serverPathMethods;

    static {
        Iterable<Class> classes = null;
        serverPathMethods = new HashMap<>();
        try {
            classes = ControllerSearcher.getClasses("com.company.controllers");
        } catch (ClassNotFoundException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(classes).forEach(klass -> {
            if (!klass.getSuperclass().equals(Controller.class)) {
                return;
            }
            for (Method method : klass.getMethods()) {
                if (method.isAnnotationPresent(ServerPath.class)) {
                    ServerPath annotation = method.getAnnotation(ServerPath.class);
                    serverPathMethods.put(annotation.path(), method);
                }
            }
        });
    }

    /**
     * Constructor del servidor
     * @param port número de puerto del servidor
     */
    Server(int port) throws IOException {
        loggedClients = new HashMap<>();
        connectedClients = new ArrayList<>();
        socketServer = new ServerSocket(port);
        serverLogger = Logger.getLogger("Server");
        TaskManager.enqueue(this::waitForData);
        TaskManager.enqueue(this::verifyConnection);

    }

    /**
     * Verifica la conexión de los clientes
     */
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
            clientsToRemove.forEach(socket -> {
                Integer objectKey = -1;
                for (Map.Entry<Integer, Socket> pair : loggedClients.entrySet()) {
                    if (pair.getValue() == socket) {
                        objectKey = pair.getKey();
                        break;
                    }
                }
                if (objectKey == -1) {
                    return;
                }
                loggedClients.remove(objectKey);
            });
        }
        try {
            Thread.sleep(100);
            TaskManager.enqueue(this::verifyConnection);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Queda en escucha de clientes
     */
    private void listenForClients() {
        while (true) {
            try {
                Socket newSocket = socketServer.accept();
                synchronized (connectedClients) {
                    connectedClients.add(newSocket);
                    connectedClients.notify();
                }

                serverLogger.info("Se conecto un cliente, tiene la IP: " + newSocket.getInetAddress());
            } catch (IOException e) {
                serverLogger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    /**
     * Corre el servidor y espera por clientes
     */
    void runForever() {
        serverLogger.info("Se inició el servidor, have fun!");
        listenForClients();
    }

    /**
     * Espera por datos de usuarios
     */
    private void waitForData() {
        while (true) {
            synchronized (connectedClients) {
                connectedClients.forEach(client -> {
                    try {
                        InputStream stream = client.getInputStream();
                        if (stream.available() != 0) {
                            byte[] b = new byte[stream.available()];
                            stream.read(b);
                            JsonParser parser = new JsonParser();
                            JsonObject request = parser.parse(new String(b, StandardCharsets.UTF_8)).getAsJsonObject();

                            TaskManager.enqueue(() -> {
                                String path = request.get("path").getAsString();
                                JsonElement methodRequest = request.get("request");

                                try {
                                    if (serverPathMethods.containsKey(path)) {
                                        int userId = -1;
                                        for (Map.Entry<Integer, Socket> pair : loggedClients.entrySet()) {
                                            if (pair.getValue() == client) {
                                                userId = pair.getKey();
                                                break;
                                            }
                                        }

                                        JsonObject response = new JsonObject();

                                        serverPathMethods.get(path).invoke(null,
                                                methodRequest,
                                                response,
                                                new ConnectionContext(connectedClients, loggedClients, client, userId));
                                        client.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
                                        return;
                                    }
                                    client.getOutputStream().write("{\"status\": \"noPath\", \"data\": {}}".getBytes(StandardCharsets.UTF_8));
                                } catch(Exception e) {
                                    serverLogger.log(Level.SEVERE, e.getMessage());
                                    try {
                                        JsonObject response = new JsonObject();
                                        response.addProperty("status", "error");
                                        JsonObject errorMessage = new JsonObject();
                                        errorMessage.addProperty("mensaje", e.getMessage());
                                        response.add("data", errorMessage);
                                        client.getOutputStream().write(response.toString().getBytes(StandardCharsets.UTF_8));
                                    } catch (IOException e1) {
                                        serverLogger.log(Level.SEVERE, e1.getMessage());
                                    }
                                }
                            });
                        }
                    }
                    catch(IOException | IllegalStateException | JsonSyntaxException | JsonIOException e) {
                        serverLogger.log(Level.SEVERE, e.getMessage());
                        try {
                            client.getOutputStream().write("{\"status\": \"error\", \"data\": {}}".getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e1) {
                            serverLogger.log(Level.SEVERE, e1.getMessage());
                        }
                    }
                });
                connectedClients.notify();
            }
        }
    }
}

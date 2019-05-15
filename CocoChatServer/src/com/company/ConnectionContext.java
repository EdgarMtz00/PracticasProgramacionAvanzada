package com.company;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

/**
 * Representa un contexto para cada petición del usuario
 */
public class ConnectionContext {
    /**
     * Lista de todos los sockets conectados
     */
    public List<Socket> connectedClients;
    /**
     * HashMap que contiene a todos los usuarios que hanniciado sesión
     */
    public HashMap<Integer, Socket> loggedClients;
    /**
     * Socket del cliente que realizó la petición
     */
    public Socket senderClient;

    public int userId;

    /**
     * Ctor que recibe todos los parametros
     * @param connectedClients lista de clientes conectados
     * @param loggedClients mapa de los clientes que han iniciado sesión
     * @param senderClient socket del cliente
     */
    public ConnectionContext(List<Socket> connectedClients,
                             HashMap<Integer, Socket> loggedClients,
                             Socket senderClient,
                             int userId) {
        this.connectedClients = connectedClients;
        this.loggedClients = loggedClients;
        this.senderClient = senderClient;
        this.userId = userId;
    }
}

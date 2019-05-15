package com.company.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que administra conexiones a una base de datos, para ello crea varias conexiones, los hilos piden conexiones y
 * cuando terminen las debe devolver
 */
public class PoolManager {
    /**
     * Pila de conexiones, contiene todas las conexiones actuales
     */
    private static final Stack<Connection> mySqlConnections;
    /**
     * Número inicial de conexiones, inicialmente 4, pueden crecer conforme se necesiten
     */
    private static final int INITIAL_CONNECTIONS = 4;
    /**
     * Nombre de usuario de la base de datos
     */
    private static final String USERNAME = "root";
    /**
     * Contraseña del usuario de la base de datos
     */
    private static final String PASSWORD = "";
    /**
     * Logger de la base de datos
     */
    private static final Logger logger;

    static {
        logger = Logger.getLogger(PoolManager.class.getName());
        mySqlConnections = new Stack<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < INITIAL_CONNECTIONS; i++) {
                Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost/cocochat?serverTimezone=UTC", USERNAME, PASSWORD);
                mySqlConnections.push(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "No se pudo conectar a la base de datos, error: " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Obtiene una conexión a la base de datos, si no hay ninguna disponible, crea una
     * @return conexión a la base de datos
     */
    public static Connection getConnection() {
        synchronized (mySqlConnections) {
            if (mySqlConnections.empty()) {
                try {
                    Connection connection = DriverManager.getConnection ("jdbc:mysql://localhost/prueba", USERNAME, PASSWORD);
                    logger.info("Se creo una conexión a la base de datos porque ya no había y el hilo " + Thread.currentThread().getId() + " solicitó una");
                    return connection;
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                    return null;
                }
            }
            logger.info("El hilo " + Thread.currentThread().getId() + " solicitá una conenxión a la bd");
            return mySqlConnections.pop();
        }
    }

    /**
     * Devuelve una conexión a la pila de conexiones
     * @param connection conexión a la base de datos que fue solicitada con "getConnection"
     */
    public static void returnConnection(Connection connection) {
        synchronized (mySqlConnections) {
            logger.info("El hilo " + Thread.currentThread().getId() + " devolvío la conexión " + connection);
            mySqlConnections.push(connection);
        }
    }
}

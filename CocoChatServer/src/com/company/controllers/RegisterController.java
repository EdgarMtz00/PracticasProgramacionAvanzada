package com.company.controllers;

import com.company.ConnectionContext;
import com.company.ServerPath;
import com.company.database.PoolManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Controlador que responde a las peticiones para iniciar sesión
 */
public class RegisterController extends Controller {

    /**
     * Método que procesa las peticiones de los usuarios en la ruta register
     * @param request Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "register")
    public static void register(JsonElement request, JsonObject response, ConnectionContext context) throws SQLException {
        JsonObject data = request.getAsJsonObject();
        String username = data.get("username").getAsString();
        String password = data.get("password").getAsString();
        String names = data.get("names").getAsString();
        String lastnames = data.get("lastnames").getAsString();

        JsonObject messageResponse = new JsonObject();

        if (username == null || password == null || names == null || lastnames == null) {
            response.addProperty("status", "error");
            response.add("data", new JsonObject());
            return;
        }

        Connection connection = PoolManager.getConnection();

        if (connection == null) {
            messageResponse.addProperty("message", "No se pudo conectar a la BD");
            response.addProperty("status", "error");
            response.add("data", messageResponse);
            return;
        }

        connection.setAutoCommit(false);

        try {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO cocochat.user(Username, Password, Names, Lastnames) values (?, ?, ?, ?)");
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, names);
            statement.setString(4, lastnames);


            int rows = statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    context.loggedClients.put(generatedKeys.getInt(1), context.senderClient);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            messageResponse.addProperty("message", "No se pudo crear el usuario");
            messageResponse.addProperty("status", "error");
            Logger.getLogger(RegisterController.class.getName()).info("No se pudo crear el usuario, causa: " + e.getMessage());
        }

        response.add("data", messageResponse);
        connection.setAutoCommit(true);
        PoolManager.returnConnection(connection);
    }

}

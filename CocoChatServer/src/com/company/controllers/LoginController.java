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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que maneja los inicios de sesión de un usuario
 */
public class LoginController extends Controller {

    /**
     * Intenta iniciar sesión a un usuario
     * @param request petición del usuario, contiene los datos que el usuario envío para efectuar la petición
     * @param response respuesta al usuario, contiene 2 paramétros: "status" y "data", "status" contiene si la petición fue exitosa o no
     *                 "data" contiene los datos que el servidor envía al cliente, en caso de existir. si no hay datos, será
     *                 un objeto json vacío
     */
    @ServerPath(path = "login")
    public static void login(JsonElement request, JsonObject response, ConnectionContext context) throws SQLException {
        JsonObject data = request.getAsJsonObject();
        String providedUsername = data.get("username").getAsString();
        String providedPassword = data.get("password").getAsString();

        response.addProperty("status", "error");
        response.add("data", new JsonObject());

        if (providedUsername == null || providedPassword == null) {
            JsonObject message = new JsonObject();
            message.addProperty("message", "No se brindaron credenciales");
            response.add("data", message);
            return;
        }

        Connection connection = PoolManager.getConnection();
        connection.setAutoCommit(true);
        try (PreparedStatement userQuery = connection.prepareStatement("SELECT Password, Username, User from user where Username = ?")) {
            userQuery.setString(1, providedUsername);
            ResultSet result = userQuery.executeQuery();
            if (result.next()) {
                String password = result.getString(1);
                String username = result.getString(2);
                int id = result.getInt(3);
                if (providedPassword.equals(password) && !context.loggedClients.containsKey(id)) {
                    JsonObject message = new JsonObject();
                    message.addProperty("message", "Acceso correcto");

                    response.addProperty("status", "ok");
                    response.addProperty("user", id);
                    response.addProperty("userName", username);
                    response.add("data", message);

                    context.loggedClients.put(id, context.senderClient);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }

        PoolManager.returnConnection(connection);

    }

    /**
     * Cierra sesión
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "logout")
    public static void logout(JsonElement request, JsonObject response, ConnectionContext context) {
        context.loggedClients.remove(context.userId);
        response.addProperty("status", "ok");
        response.add("data", new JsonObject());
    }
}

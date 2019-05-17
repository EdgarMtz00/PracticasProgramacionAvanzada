package com.company.controllers;

import com.company.ConnectionContext;
import com.company.ServerPath;
import com.company.database.PoolManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que gestiona a los usuarios que no son tus amigos
 */
public class UsersController extends Controller {

    /**
     * Devuelve la lista de usuarios que no son tus amigos y no tienen la sesión iniciada
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "users")
    public static void users(JsonElement request, JsonObject response, ConnectionContext context) throws SQLException {
        Connection connection = null;
        try {
            connection = PoolManager.getConnection();
            JsonArray data = new JsonArray();
            PreparedStatement statement = connection.prepareStatement("select User, Username from user\n" +
                    "where username not in (select\n" +
                    "replace(concat(u.Username, u2.Username), (select Username from user where User.User = ?), \"\") as Username\n" +
                    "from Friends_Chat as f\n" +
                    "       inner join user as u on f.User_SR = u.User\n" +
                    "       inner join user as u2 on f.User_RR = u2.User\n" +
                    "where (((u.user = ? && u2.User != ?) || (u.user != ? && u2.User = ?)) && Filter_Type = 'Amigos')) && user!=?;");
            statement.setInt(1, context.userId);
            statement.setInt(2, context.userId);
            statement.setInt(3, context.userId);
            statement.setInt(4, context.userId);
            statement.setInt(5, context.userId);
            statement.setInt(6, context.userId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (!context.loggedClients.containsKey(result.getInt(1)))
                    data.add(result.getString(2));
            }
            if (data.size() == 0) {
                response.addProperty("status", "error");
                data.add("No se encontraron elementos");
            } else {
                response.addProperty("status", "ok");
            }
            response.add("data", data);
        } catch (SQLException e) {
            PoolManager.returnConnection(connection);
            JsonArray array = new JsonArray();
            array.add("No se econtraron elementos");
            response.addProperty("status", "error");
            response.addProperty("message", "error al seleccionar usuario");
            response.add("data", array);
            e.printStackTrace();
            throw e;
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Devuelve la lista de usuarios que no son tus amigos y tienen la sesión iniciada
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "users/connected")
    public static void getConnectedUsers(JsonElement request, JsonObject response, ConnectionContext context) {
        Connection connection = PoolManager.getConnection();
        try {
            JsonArray data = new JsonArray();
            PreparedStatement statement = connection.prepareStatement("select User, Username from user\n" +
                    "  where username not in (select\n" +
                    "replace(concat(u.Username, u2.Username), (select username from user where User.User = ?), \"\") as User\n" +
                    "from Friends_Chat as f\n" +
                    "       inner join user as u on f.User_SR = u.User\n" +
                    "       inner join user as u2 on f.User_RR = u2.User\n" +
                    "where (((u.user = ? && u2.User != ?) || (u.user != ? && u2.User = ?)) && Filter_Type = 'Amigos')) && user!=?");
            statement.setInt(1, context.userId);
            statement.setInt(2, context.userId);
            statement.setInt(3, context.userId);
            statement.setInt(4, context.userId);
            statement.setInt(5, context.userId);
            statement.setInt(6, context.userId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                if (context.loggedClients.containsKey(result.getInt(1))) {
                    data.add(result.getString(2)); // usuarios
                }
            }
            if (data.size() == 0) {
                response.addProperty("status", "error");
                data.add("No se encontraron elementos");

            } else {
                response.addProperty("status", "ok");
            }
            response.addProperty("connected", "true");
            response.add("data", data);
        } catch (SQLException e) {
            JsonArray array = new JsonArray();
            array.add("No se encontraron elementos");
            response.addProperty("status", "error");
            response.add("data", array);
            e.printStackTrace();
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Realiza una petición de conversación
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "users/request")
    public static void requestUserChat(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();

        try {
            JsonObject req = request.getAsJsonObject();
            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, req.get("user").getAsString());
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int userId = set.getInt(1);

            statement = connection.prepareStatement("SELECT Username from user where User = ?");
            statement.setInt(1, context.userId);
            set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario actual, esto no debe de pasar");
            }
            String username = set.getString(1);
            if (!context.loggedClients.containsKey(userId)) {
                throw new Exception("No esta conectado...");
            }
            JsonObject userStart = new JsonObject();
            userStart.addProperty("tipo", "solicitud");
            userStart.addProperty("mensaje", username + " ha solicitad chatear contigo\n" +
                    "Ingresa a su chat en la pestaña usuarios para chatear con el, xfas");
            OutputStream stream = context.loggedClients
                    .get(userId)
                    .getOutputStream();
            stream.write(userStart.toString().getBytes(StandardCharsets.UTF_8));
            stream.flush();
        } catch (Exception e) {
            PoolManager.returnConnection(connection);
            throw e;
        }
        response.addProperty("status", "ok");
        response.add("data", new JsonObject());

        PoolManager.returnConnection(connection);
    }

    /**
     * Envía un mensaje a un usuario que no tengas agregado
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "users/sendMessage")
    public static void sendMessage(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();

        try {
            JsonObject req = request.getAsJsonObject();
            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, req.get("user").getAsString());
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int userId = set.getInt(1);

            statement = connection.prepareStatement("SELECT Username from user where User = ?");
            statement.setInt(1, context.userId);
            set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario actual, esto no debe de pasar");
            }
            if (!context.loggedClients.containsKey(userId)) {
                throw new Exception("No esta conectado...");
            }

            // obtener nombre del usuario actual
            statement = connection.prepareStatement("SELECT Username from user where User = ?");
            statement.setInt(1, context.userId);
            set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            String name = set.getString(1);
            JsonObject userStart = new JsonObject();
            userStart.addProperty("tipo", "mensaje");
            userStart.addProperty("mensaje", req.get("message").getAsString());
            userStart.addProperty("remitente", name);
            OutputStream stream = context.loggedClients
                    .get(userId)
                    .getOutputStream();
            stream.write(userStart.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            PoolManager.returnConnection(connection);
            throw e;
        }
        response.addProperty("status", "ok");
        response.add("data", new JsonObject());
        PoolManager.returnConnection(connection);
    }
}

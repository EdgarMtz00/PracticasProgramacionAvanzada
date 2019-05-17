package com.company.controllers;

import com.company.ConnectionContext;
import com.company.ServerPath;
import com.company.database.PoolManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Clase que gestiona las amistades y sus mensajes
 */
public class FriendsController extends Controller {

    /**
     * Devuelve los amigos del que realizó la petición a este método
     *
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends")
    public static void friends(JsonElement request, JsonObject response, ConnectionContext context) {
        Connection connection = PoolManager.getConnection();

        try {
            if (connection == null) {
                throw new SQLException("Conexión nula");
            }
            PreparedStatement statement = connection.prepareStatement("select\n" +
                    "       replace(concat(u.Username, u2.Username), (select Username from user where User.User = ?), \"\") as Username,\n" +
                    "replace(concat(u.User, u2.User), (select user from user where User.User = ?), \"\") as User\n" +
                    "    from Friends_Chat as f\n" +
                    "    inner join user as u on f.User_SR = u.User\n" +
                    "    inner join user as u2 on f.User_RR = u2.User\n" +
                    "    where (((u.user = ? && u2.User != ?) || (u.user != ? && u2.User = ?)) && Filter_Type = 'Amigos')");

            statement.setInt(1, context.userId);
            statement.setInt(2, context.userId);
            statement.setInt(3, context.userId);
            statement.setInt(4, context.userId);
            statement.setInt(5, context.userId);
            statement.setInt(6, context.userId);
            ResultSet result = statement.executeQuery();
            JsonArray array = new JsonArray();
            while (result.next()) {
                if (!context.loggedClients.containsKey(result.getInt(2))) {
                    String name = result.getString(1);
                    array.add(name);
                }
            }
            response.add("data", array);
            response.addProperty("status", "ok");
        } catch (SQLException e) {
            Logger.getLogger(FriendsController.class.getName(), e.getMessage());
            response.add("data", new JsonArray());
            response.addProperty("status", "error");
        }

        PoolManager.returnConnection(connection);
    }

    /**
     * Devuelve los amigos que estan conectados
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente, consiste en status y un arreglo de Json
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/connected")
    public static void connected_friends(JsonElement request, JsonObject response, ConnectionContext context) {
        Connection connection = PoolManager.getConnection();
        try {
            if (connection == null) {
                throw new SQLException("Conexión nula");
            }

            PreparedStatement statement = connection.prepareStatement("select\n" +
                    "       replace(concat(u.Username, u2.Username), (select Username from user where User.User = ?), \"\") as Username,\n" +
                    "replace(concat(u.User, u2.User), (select user from user where User.User = ?), \"\") as User\n" +
                    "    from Friends_Chat as f\n" +
                    "    inner join user as u on f.User_SR = u.User\n" +
                    "    inner join user as u2 on f.User_RR = u2.User\n" +
                    "    where (((u.user = ? && u2.User != ?) || (u.user != ? && u2.User = ?)) && Filter_Type = 'Amigos')");

            statement.setInt(1, context.userId);
            statement.setInt(2, context.userId);
            statement.setInt(3, context.userId);
            statement.setInt(4, context.userId);
            statement.setInt(5, context.userId);
            statement.setInt(6, context.userId);
            ResultSet result = statement.executeQuery();
            JsonArray array = new JsonArray();
            while (result.next()) {
                if (context.loggedClients.containsKey(result.getInt(2))) {
                    String name = result.getString(1);
                    array.add(name);
                }
            }
            response.add("data", array);
            response.addProperty("status", "ok");
            response.addProperty("connected", "true");
        } catch (SQLException e) {
            Logger.getLogger(FriendsController.class.getName(), e.getMessage());
            response.add("data", new JsonArray());
            response.addProperty("status", "error");
        }

        PoolManager.returnConnection(connection);
    }

    /**
     * Añade a un amigo que previamente envio una solicitud de amistad
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/add")
    public static void sendFriendRequest(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();
        JsonObject req = request.getAsJsonObject();
        try {
            assert connection != null;
            connection.setAutoCommit(false);

            String friendName = req.get("user").getAsString();

            PreparedStatement getID = connection.prepareStatement("SELECT User from user where Username = ?");
            getID.setString(1, friendName);
            ResultSet set = getID.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int friendId = set.getInt(1);

            PreparedStatement existsFriendshipOrRequest = connection.prepareStatement("select *\n" +
                    "from friends_chat where (Filter_Type = 'Solicitud' OR Filter_Type = 'Amistad') AND User_SR = ? AND User_RR = ?");
            existsFriendshipOrRequest.setInt(1, context.userId);
            existsFriendshipOrRequest.setInt(2, friendId);
            ResultSet query = existsFriendshipOrRequest.executeQuery();
            if (query.next()) {
                throw new Exception("Ya hay una amistad o solicitud");
            }

            PreparedStatement statement = connection.prepareStatement("insert into friends_chat (User_SR, User_RR, Filter_Type, Message, Msg_Time)\n" +
                    "values (?, ?, 'Solicitud', '', NOW())");
            statement.setInt(1, context.userId);
            statement.setInt(2, friendId);
            statement.executeUpdate();
            connection.commit();

            if (context.loggedClients.containsKey(friendId)) {
                JsonObject r = new JsonObject();
                r.addProperty("type", "solicitud");
                BufferedOutputStream stream = new BufferedOutputStream(context.loggedClients
                        .get(friendId)
                        .getOutputStream());
                stream.write(r.toString().getBytes(StandardCharsets.UTF_8));
                stream.flush();
            }
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
        } catch (Exception e) {
            PoolManager.returnConnection(connection);
            connection.rollback();
            throw e;
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Elimina a un amigo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/delete")
    public static void removeFriend(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();
        try {
            connection.setAutoCommit(false);
            JsonObject o = request.getAsJsonObject();
            String friend = o.get("friend").getAsString();
            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, friend);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new Exception("No existe el usuario con ese id");
            }
            int friendId = resultSet.getInt(1);

            statement = connection.prepareStatement("select PK_Log \n" +
                    "from friends_chat where User_SR = ? and User_RR = ? and Filter_Type = 'Amigos'");

            statement.setInt(1, context.userId);
            statement.setInt(2, friendId);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                // ok, aquí esta la relación
                statement = connection.prepareStatement("delete from friends_chat WHERE PK_Log = ?");
                statement.setInt(1, id);
                statement.executeUpdate();
            } else {
                statement = connection.prepareStatement("select PK_Log \n" +
                        "from friends_chat where User_SR = ? and User_RR = ? and Filter_Type = 'Amigos'");

                statement.setInt(1, friendId);
                statement.setInt(2, context.userId);

                resultSet = statement.executeQuery();
                if (!resultSet.next()) {
                    throw new Exception("No son amigos");
                }

                int id = resultSet.getInt(1);
                // ok, aquí esta la relación
                statement = connection.prepareStatement("delete from friends_chat WHERE PK_Log = ?");
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            connection.setAutoCommit(true);
            PoolManager.returnConnection(connection);
            throw e;
        }

        PoolManager.returnConnection(connection);
    }

    /**
     * Envía un mensaje a un amigo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/sendMessage")
    public static void sendMessage(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = null;
        try {
            connection = PoolManager.getConnection();
            connection.setAutoCommit(false);
            JsonObject req = request.getAsJsonObject();

            String friendName = req.get("friend").getAsString();

            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, friendName);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int friendId = set.getInt(1);

            statement = connection.prepareStatement("insert into friends_chat (User_SR, User_RR, Filter_Type, Message, Msg_Time)\n" +
                    "values (?, ?, 'Mensaje', ?, NOW())");
            statement.setInt(1, context.userId);
            statement.setInt(2, friendId);
            statement.setString(3, req.get("message").getAsString());
            statement.executeUpdate();

            statement = connection.prepareStatement("SELECT Username from user where User = ?");
            statement.setInt(1, context.userId);
            set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            String name = set.getString(1);

            if (context.loggedClients.containsKey(friendId)) {
                JsonObject messageEvent = new JsonObject();
                messageEvent.addProperty("tipo", "mensaje");
                messageEvent.addProperty("mensaje", req.get("message").getAsString());
                messageEvent.addProperty("remitente", name);
                OutputStream stream = context.loggedClients.get(friendId).getOutputStream();
                stream.write(messageEvent.toString().getBytes(StandardCharsets.UTF_8));
            }

            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
            connection.commit();
        } catch (Exception e) {
            connection.setAutoCommit(true);
            PoolManager.returnConnection(connection);
            throw e;
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Obtiene todas las peticiones de amistad de un usuario
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/requests")
    public static void getFriendRequests(JsonElement request, JsonObject response, ConnectionContext context) throws SQLException {
        Connection connection = PoolManager.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("select User_SR as User, Username\n" +
                    "from friends_chat inner join user u on friends_chat.User_SR = u.User where User_RR = ? and Filter_Type = 'Solicitud'");
            statement.setInt(1, context.userId);
            JsonArray array = new JsonArray();
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                String username = set.getString(2);
                array.add(username);
            }
            response.addProperty("status", "ok");
            response.add("data", array);
        } catch (Exception e) {
            PoolManager.returnConnection(connection);
            throw e;
        }

        PoolManager.returnConnection(connection);
    }

    /**
     * Acepta una solicitud de amistad
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/accept")
    public static void acceptFriendRequest(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();

        try {
            assert connection != null;
            JsonObject req = request.getAsJsonObject();
            String friendName = req.get("user").getAsString();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, friendName);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int personId = set.getInt(1);
            statement = connection.prepareStatement("update friends_chat\n" +
                    "set Filter_Type = 'Amigos'\n" +
                    "where User_SR = ? and User_RR = ? and Filter_Type = 'Solicitud'");
            statement.setInt(1, personId);
            statement.setInt(2, context.userId);
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            connection.setAutoCommit(true);
            PoolManager.returnConnection(connection);
            throw e;
        }
        connection.setAutoCommit(true);

        PoolManager.returnConnection(connection);
    }

    /**
     * Rechaza una petición de amistad
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "friends/decline")
    public static void declineFriendRequest(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();

        try {
            JsonObject req = request.getAsJsonObject();
            String friendName = req.get("user").getAsString();
            connection.setAutoCommit(false);

            PreparedStatement statement = connection.prepareStatement("SELECT User from user where Username = ?");
            statement.setString(1, friendName);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int personId = set.getInt(1);
            statement = connection.prepareStatement("delete\n" +
                    "from friends_chat\n" +
                    "where User_SR = ?\n" +
                    "  and User_RR = ?\n" +
                    "  and Filter_Type = 'Solicitud'");
            statement.setInt(1, personId);
            statement.setInt(2, context.userId);
            statement.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            connection.setAutoCommit(true);
            PoolManager.returnConnection(connection);
            throw e;
        }
        connection.setAutoCommit(true);

        PoolManager.returnConnection(connection);
    }
}

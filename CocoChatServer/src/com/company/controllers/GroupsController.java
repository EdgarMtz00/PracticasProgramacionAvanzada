package com.company.controllers;

import com.company.ConnectionContext;
import com.company.ServerPath;
import com.company.database.PoolManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que se encarga de la gestión de grupos, enviar mensajes, agregar y eliminar miembros
 */
public class GroupsController extends Controller {

    /**
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups")
    public static void groups(JsonElement request, JsonObject response, ConnectionContext context) {
        Connection connection = PoolManager.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("select g.Group_Name from chat_messages cm" +
                    " inner join Groups g on cm.Group_N = G.PK_Group" +
                    " where cm.User = ? && cm.Filter_Type = 'Ingreso'");
            statement.setInt(1, context.userId);

            JsonArray array = new JsonArray();
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                String groupName = results.getString(1);
                System.out.println(groupName);
                array.add(groupName);
            }
            if (array.size() == 0) {
                response.addProperty("status", "error");
                array.add("No se encontraron elementos");
            } else {
                response.addProperty("status", "ok");
            }
            response.add("data", array);

        } catch (SQLException e) {
            PoolManager.returnConnection(connection);
            Logger.getLogger(GroupsController.class.getName()).log(Level.SEVERE, e.getMessage());
            response.addProperty("status", "ok");
            response.add("data", new JsonArray());
        }

        PoolManager.returnConnection(connection);
    }

    /**
     * Devuelve los mensajes de un grupo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups/messages")
    public static void getMessages(JsonElement request, JsonObject response, ConnectionContext context) {
        Connection connection = PoolManager.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement("select Message, Msg_Time, u.Username as User\n" +
                    "from chat_messages\n" +
                    "       inner join user u on chat_messages.User = u.User\n" +
                    "where Group_N = ?\n" +
                    "  AND Filter_Type = 'Mensaje'");
            statement.setInt(1, context.userId);
            ResultSet set = statement.executeQuery();
            JsonArray array = new JsonArray();
            while (set.next()) {
                String message = set.getString(1);
                Date time = set.getDate(2);
                String user = set.getString(3);
                JsonObject messageInfo = new JsonObject();
                messageInfo.addProperty("mensaje", message);
                messageInfo.addProperty("hora", time.toString());
                messageInfo.addProperty("usuario", user);
                array.add(messageInfo);
            }
            response.addProperty("status", "ok");
            response.add("data", array);
        } catch (SQLException e) {
            Logger.getLogger(GroupsController.class.getName(), e.getMessage());
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Envía un mensaje a un grupo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups/sendMessage")
    public static void sendMessage(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        JsonObject req = request.getAsJsonObject();
        Connection connection = null;
        try {
            connection = PoolManager.getConnection();
            connection.setAutoCommit(false);
            int groupId = -1;
            String mensaje = req.get("message").getAsString();
            String  group = req.get("group").getAsString();

            PreparedStatement getGroupID =connection.prepareStatement("SELECT PK_Group from `groups` where Group_Name = ?");
            getGroupID.setString(1, group);
            ResultSet result = getGroupID.executeQuery();
            if(result.next()){
                groupId = result.getInt(1);
            }

            PreparedStatement statement = connection.prepareStatement("insert into chat_messages (Filter_Type, Message, Msg_Time, User, Group_N)\n" +
                    "values ('Mensaje', ?, NOW(), ?, ?)");
            statement.setString(1, mensaje);
            statement.setInt(2, context.userId);
            statement.setInt(3, groupId);
            statement.executeUpdate();
            connection.commit();

            String senderUsername = getUsername(connection, context.userId);

            PreparedStatement getGroupMembers = connection.prepareStatement("select User\n" +
                    "from chat_messages where Group_N = ? AND Filter_Type = 'Ingreso'");
            getGroupMembers.setInt(1, groupId);

            ResultSet set = getGroupMembers.executeQuery();
            while (set.next()) {
                int uId = set.getInt(1);
                if (!context.loggedClients.containsKey(uId) || uId == context.userId) {
                    continue;
                }
                OutputStream stream = context.loggedClients.get(uId).getOutputStream();
                JsonObject message = new JsonObject();
                message.addProperty("grupo", groupId);
                message.addProperty("tipo", "mensaje");
                message.addProperty("mensaje", mensaje);
                message.addProperty("remitente", senderUsername);
                stream.write(message.toString().getBytes(StandardCharsets.UTF_8));
            }
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
        } catch (SQLException | IOException e) {
            PoolManager.returnConnection(connection);
            connection.rollback();
            throw new Exception(e.getMessage());
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Crea un grupo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups/create")
    public static void createGroup(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();
        JsonObject req = request.getAsJsonObject();

        PreparedStatement statement = null;
        connection.setAutoCommit(false);
        try {
            String friendName = req.get("friend").getAsString();
            PreparedStatement getFriendID = connection.prepareStatement("SELECT User from user where Username = ?");
            getFriendID.setString(1, friendName);
            ResultSet set = getFriendID.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int friendId = set.getInt(1);

            statement = connection.prepareStatement("insert into groups (Group_Name)\n" +
                    "values (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, req.get("nombre").getAsString());
            int rows = statement.executeUpdate();
            ResultSet insertedKeys = statement.getGeneratedKeys();
            if (insertedKeys.next()) {
                int groupId = insertedKeys.getInt(1);
                PreparedStatement insertUser = connection.prepareStatement("insert into chat_messages (Filter_Type, Message, Msg_Time, User, Group_N)\n" +
                        "values ('Ingreso', '', NOW(), ?, ?), ('Ingreso', '', NOW(), ?, ?)");
                insertUser.setInt(1, context.userId);
                insertUser.setInt(2, groupId);
                insertUser.setInt(3, friendId);
                insertUser.setInt(4, groupId);
                insertUser.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
        } catch (SQLException e) {
            PoolManager.returnConnection(connection);
            connection.rollback();
            throw new Exception(e.getMessage());
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Añade un usuario a un grupo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups/add")
    public static void addUser(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();
        JsonObject req = request.getAsJsonObject();

        PreparedStatement statement = null;
        connection.setAutoCommit(false);
        try {
            String friendName = req.get("friend").getAsString();
            String group = req.get("group").getAsString();

            PreparedStatement getFriendID = connection.prepareStatement("SELECT User from user where Username = ?");
            getFriendID.setString(1, friendName);
            ResultSet set = getFriendID.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el usuario");
            }
            int friendId = set.getInt(1);

            PreparedStatement getGroupID = connection.prepareStatement("Select PK_Group from `groups` where Group_Name = ?");
            getGroupID.setString(1, group);
            set = getGroupID.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el  grupo");
            }
            int groupId = set.getInt(1);

            PreparedStatement insertUser = connection.prepareStatement("insert into chat_messages (Filter_Type, Message, Msg_Time, User, Group_N)\n" +
                    "values ('Ingreso', '', NOW(), ?, ?)");
            insertUser.setInt(1, friendId);
            insertUser.setInt(2, groupId);
            insertUser.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
        } catch (SQLException e) {
            PoolManager.returnConnection(connection);
            connection.rollback();
            throw new Exception(e.getMessage());
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Abandona un grupo
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "groups/leave")
    public static void leaveGroup(JsonElement request, JsonObject response, ConnectionContext context) throws Exception {
        Connection connection = PoolManager.getConnection();
        JsonObject req =request.getAsJsonObject();
        try {
            String group = req.get("group").getAsString();
            PreparedStatement getGroupID = connection.prepareStatement("Select PK_Group from `groups` where Group_Name = ?");
            getGroupID.setString(1, group);
            ResultSet set = getGroupID.executeQuery();
            if (!set.next()) {
                throw new Exception("No existe el  grupo");
            }
            int groupId = set.getInt(1);

            PreparedStatement statement = connection.prepareStatement("DELETE from chat_messages where User = ? " +
                    "and Filter_Type = 'Ingreso' and Group_N = ?");
            statement.setInt(1, context.userId);
            statement.setInt(2, groupId);
            statement.executeUpdate();
            response.addProperty("status", "ok");
            response.add("data", new JsonObject());
        }catch (SQLException e){
            response.addProperty("status", "error");
            response.add("data", new JsonObject());
        }
    }

    /**
     * Obtiene el nombre de usuario por medio del id de usuario
     * @param connection consulta a la base de datos
     * @param id identificador del usuario
     * @return Nombre del usuario, cadena vacia si no lo encuentra
     */
    private static String getUsername(Connection connection, int id) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM user WHERE User = ?");
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return set.getString(2);
            }
        } catch (SQLException e) {
            return "";
        }
        return "";
    }
}

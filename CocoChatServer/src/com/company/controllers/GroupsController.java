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
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            response.addProperty("status", "ok");
            response.add("data", array);

        } catch (SQLException e) {
            Logger.getLogger(GroupsController.class.getName()).log(Level.SEVERE, e.getMessage());
            response.addProperty("status", "ok");
            response.add("data", new JsonArray());
        }

        PoolManager.returnConnection(connection);
    }

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
    }

    @ServerPath(path = "groups/sendMessage")
    public static void sendMessage(JsonElement request, JsonObject response, ConnectionContext context) throws SQLException {
        Connection connection = PoolManager.getConnection();
        JsonObject req = request.getAsJsonObject();
        try {
            connection.setAutoCommit(false);
            String mensaje = req.get("mensaje").getAsString();
            int groupId = req.get("grupo").getAsInt();

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
                if (!context.loggedClients.containsKey(uId)) {
                    continue;
                }
                BufferedOutputStream stream = new BufferedOutputStream(context.loggedClients.get(uId).getOutputStream());
                JsonObject message = new JsonObject();
                message.addProperty("grupo", groupId);
                message.addProperty("mensaje", mensaje);
                message.addProperty("remitente", senderUsername);
                stream.write(message.toString().getBytes(StandardCharsets.UTF_8));
                stream.flush();
            }
        } catch (SQLException | IOException e) {
            connection.rollback();
        }
    }

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


interface ChatEventListener {
    boolean shouldReceiveMessage(JsonObject serverEvent);
    void execute();
}

// en lo que maneja el socket del cliente


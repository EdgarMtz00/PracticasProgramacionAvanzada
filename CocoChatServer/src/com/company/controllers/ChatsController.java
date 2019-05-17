package com.company.controllers;

import com.company.ConnectionContext;
import com.company.ServerPath;
import com.company.database.PoolManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que gestiona todo lo relacionado a los chats
 */
public class ChatsController extends Controller {

    /**
     * @param request  Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context  contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
    @ServerPath(path = "chats")
    public static void chats(JsonElement request, JsonObject response, ConnectionContext context) {
        JsonObject userData = request.getAsJsonObject();
        Connection connection = PoolManager.getConnection();
        try {
            connection.setAutoCommit(true);
            JsonArray data = new JsonArray();
            if (userData.has("friend")) {
                int friendId = -1;
                PreparedStatement getId = connection.prepareStatement("SELECT User from user where Username = ?");
                getId.setString(1, userData.get("friend").getAsString());
                ResultSet result = getId.executeQuery();
                if (result.next()) {
                    friendId = result.getInt(1);
                }
                PreparedStatement statement = connection.prepareStatement("select user.Username, Message, Msg_Time " +
                        "from friends_chat inner join user on User = friends_chat.User_SR where (User_RR = ? and User_SR = ?)" +
                        " or (User_SR = ? and User_RR = ?) and Filter_Type = 'Mensaje' order by Msg_Time");
                statement.setInt(1, context.userId);
                statement.setInt(2, friendId);
                statement.setInt(3, context.userId);
                statement.setInt(4, friendId);
                getResult(data, statement);
                /*statement = connection.prepareStatement("select user.Username, Message, Msg_Time " +
                        "from friends_chat inner join user on User = friends_chat.User_SR where User_RR = ? and User_SR = ? and Filter_Type = 'Mensaje'");
                statement.setInt(1, friendId);
                statement.setInt(2, context.userId);
                getResult(data, statement);*/
            } else if (userData.has("group")) {
                int groupId = -1;

                PreparedStatement getGroupID =connection.prepareStatement("SELECT PK_Group from `groups` where Group_Name = ?");
                getGroupID.setString(1, userData.get("group").getAsString());
                ResultSet result = getGroupID.executeQuery();
                if(result.next()){
                    groupId = result.getInt(1);
                }

                PreparedStatement statement = connection.prepareStatement("select user.username, Message, Msg_Time\n" +
                        "from chat_messages\n" +
                        "       INNER JOIN user on chat_messages.User = user.User\n" +
                        "where Group_N = ?\n" +
                        "  and Filter_Type = 'Mensaje' order by Msg_Time");
                statement.setInt(1, groupId);
                getResult(data, statement);
            }
            response.addProperty("status", "ok");
            response.add("data", data);
        } catch (SQLException e) {
            JsonArray array = new JsonArray();
            array.add("No se encontraro");
            response.addProperty("status", "error");
            response.add("data", array);
            e.printStackTrace();
        }
        PoolManager.returnConnection(connection);
    }

    /**
     * Obtiene el resultado de una busqueda de usuariosmo un arrgllo Json
     * @param data arreglo al que se le agregarán elementos
     * @param statement query que se utilizará
     * @throws SQLException En caso de que no exista algún campo
     */
    private static void getResult(JsonArray data, PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            JsonObject o = new JsonObject();
            o.addProperty("user", result.getString(1));
            o.addProperty("message", result.getString(2));
            o.addProperty("msgTime", result.getString(3));
            data.add(o);
        }
    }
}

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

public class ChatsController extends Controller {

    /**
     * @param request Objeto Json con los datos de la petición del usuario
     * @param response Respuesta Json que se le enviará al cliente
     * @param context contexto de la petición
     * @throws SQLException Excepción SQL en caso de que no pueda obtener una conexión
     */
   @ServerPath(path = "chats")
    public static void chats(JsonElement request, JsonObject response, ConnectionContext context) {
       JsonObject userData = request.getAsJsonObject();
       Connection connection = PoolManager.getConnection();
       try {
           JsonArray data = new JsonArray();
           if (userData.has("friend")) {
               PreparedStatement statement = connection.prepareStatement("select User_SR, Message, Msg_Time " +
                       "from friends_chat where User_RR = ? and User_SR = ?");
               statement.setString(1, userData.get("user").getAsString());
               statement.setString(2, userData.get("friend").getAsString());
               getResult(data, statement);
               statement = connection.prepareStatement("select User_SR, Message, Msg_Time " +
                       "from friends_chat where User_RR = ? and User_SR = ?");
               statement.setString(1, userData.get("friend").getAsString());
               statement.setString(2, userData.get("user").getAsString());
               getResult(data, statement);
           }else if (userData.has("group")){
               PreparedStatement statement = connection.prepareStatement("select User, Message, Msg_Time " +
                       "from chat_messages where Group_N = ? and Filter_Type = 'Mensaje'");
               statement.setString(1, userData.get("group").getAsString());
               getResult(data, statement);
           }
           response.addProperty("status", "ok");
           response.add("data", data);
       }catch (SQLException e) {
           response.addProperty("status", "error");
           e.printStackTrace();
       }
   }

    private static void getResult(JsonArray data, PreparedStatement statement) throws SQLException {
        ResultSet result = statement.executeQuery();
        while (result.next()){
            JsonObject o = new JsonObject();
            o.addProperty("user", result.getInt(1));
            o.addProperty("message", result.getString(2));
            o.addProperty("msgTime", result.getString(3));
            data.add(o);
        }
    }
}

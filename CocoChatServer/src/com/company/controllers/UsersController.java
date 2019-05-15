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

public class UsersController extends Controller{
    @ServerPath(path = "users")
    public static void users(JsonElement request, JsonObject response, ConnectionContext context) {
        try {
            JsonArray data = new JsonArray();
            Connection connection = PoolManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("Select User, Username from user");
            ResultSet result = statement.executeQuery();
            while (result.next()){
                JsonObject o = new JsonObject();
                o.addProperty("user", result.getInt(1));
                o.addProperty("userName", result.getString(2));
                data.add(o);
            }
            response.addProperty("status", "ok");
            response.add("data", data);
        } catch (SQLException e) {
            response.addProperty("status", "error");
            response.addProperty("message", "error al seleccionar usuario");
            e.printStackTrace();
        }
    }

    @ServerPath(path = "users/connected")
    public static void getConnectedUsers(JsonElement request, JsonObject response, ConnectionContext context) {
        try {
            JsonArray data = new JsonArray();
            Connection connection = PoolManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("Select User, Username from user");
            ResultSet result = statement.executeQuery();
            while (result.next()){
                if(context.loggedClients.containsKey(result.getInt(1))) {
                    JsonObject o = new JsonObject();
                    o.addProperty("user", result.getInt(1));
                    o.addProperty("userName", result.getString(2));
                    data.add(o);
                }
            }
            response.addProperty("status", "ok");
            response.add("data", data);
        } catch (SQLException e) {
            response.addProperty("status", "error");
            response.addProperty("data", "error al seleccionar usuario");
            e.printStackTrace();
        }
    }
}

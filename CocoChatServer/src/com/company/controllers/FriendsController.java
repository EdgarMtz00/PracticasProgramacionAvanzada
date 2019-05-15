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
import java.util.logging.Logger;

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
            PreparedStatement statement = connection.prepareStatement("select replace(concat(u.Username, u2.Username), (select Username from user where User.User=?), \"\") as Users from Friends_Chat as f\n" +
                    "  inner join user as u\n" +
                    "    on f.User_SR = u.User\n" +
                    "  inner join user as u2\n" +
                    "    on f.User_RR = u2.User\n" +
                    "where (((u.user=? && u2.User!=?) || (u.user!=? && u2.User=?)) && Filter_Type='Amigos')");

            statement.setInt(1, context.userId);
            statement.setInt(2, context.userId);
            statement.setInt(3, context.userId);
            statement.setInt(4, context.userId);
            statement.setInt(5, context.userId);
            ResultSet result = statement.executeQuery();
            JsonArray array = new JsonArray();
            while (result.next()) {
                String name = result.getString(1);
                array.add(name);
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
}

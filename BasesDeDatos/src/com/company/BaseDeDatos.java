package com.company;

import java.sql.*;

public class BaseDeDatos {
    private Connection con;
    public BaseDeDatos(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/escuela", "root", "");
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM alumnos");
            ResultSet data = stmt.executeQuery();
            while(data.next()){
                System.out.println(data.getString("nombre"));
                System.out.println(data.getInt("Registro"));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}

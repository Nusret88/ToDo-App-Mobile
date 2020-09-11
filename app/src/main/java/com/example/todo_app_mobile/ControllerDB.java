package com.example.todo_app_mobile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ControllerDB {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {


        String db = "jdbc:postgresql://ec2-34-251-118-151.eu-west-1.compute.amazonaws.com:5432/d1s3fkge434rkv";
        String user = "fgwmzdhzngqkcq";
        String pass = "4efd9b17582b1148f35943d8093d808826779b9e996df15436e9aad7b2d378a1";


        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(""+db+"", ""+user+"", ""+pass+"");
            System.out.println("Getting Connection");

        }

        catch(Exception e) {
            e.printStackTrace();
            connection.close();
        }

        System.out.println("Connected to DB");
        return connection;

    }

}

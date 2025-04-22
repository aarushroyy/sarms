package com.sarms.test;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/sarms?useSSL=false&serverTimezone=UTC",
                    "sarmsuser",
                    "password"
            );
            System.out.println("Connection successful!");
            conn.close();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
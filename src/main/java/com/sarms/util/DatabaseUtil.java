//// src/main/java/com/sarms/util/DatabaseUtil.java
//package com.sarms.util;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Properties;
//
//public class DatabaseUtil {
//    private static Properties properties = new Properties();
//
//    static {
//        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("config/database.properties")) {
//            if (input == null) {
//                throw new RuntimeException("Unable to find database.properties file");
//            }
//            properties.load(input);
//            Class.forName(properties.getProperty("db.driver"));
//        } catch (IOException | ClassNotFoundException e) {
//            throw new RuntimeException("Error loading database configuration: " + e.getMessage(), e);
//        }
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(
//                properties.getProperty("db.url"),
//                properties.getProperty("db.username"),
//                properties.getProperty("db.password")
//        );
//    }
//
//    public static void closeConnection(Connection connection) {
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException e) {
//                System.err.println("Error closing connection: " + e.getMessage());
//            }
//        }
//    }
//}

package com.sarms.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtil {
    private static Properties properties = new Properties();
    private static boolean initialized = false;

    static {
        try (InputStream input = DatabaseUtil.class.getClassLoader().getResourceAsStream("config/database.properties")) {
            if (input == null) {
                System.err.println("Unable to find database.properties file. Make sure it exists in src/main/resources/config/");
                throw new RuntimeException("Unable to find database.properties file");
            }

            properties.load(input);
            String driverClass = properties.getProperty("db.driver");
            System.out.println("Loading JDBC driver: " + driverClass);
            Class.forName(driverClass);
            initialized = true;
            System.out.println("Database configuration loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            throw new RuntimeException("Error loading database configuration: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("Error loading database configuration: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Unexpected error loading database configuration: " + e.getMessage());
            throw new RuntimeException("Error loading database configuration: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new SQLException("Database utility not properly initialized");
        }

        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");

            System.out.println("Connecting to database: " + url);
            System.out.println("Using username: " + username);

//            return DriverManager.getConnection(url, username, password);
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
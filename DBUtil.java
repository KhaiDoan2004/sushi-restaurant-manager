package com.example.demo1.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/sushi_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "Anhlaso1@";

    public static Connection getConnection() throws SQLException {
        try {
            // Với JDBC 4.0+, driver tự động load, nhưng vẫn nên giữ để đảm bảo tương thích
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("DBUtil: MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("DBUtil ERROR: MySQL JDBC Driver not found");
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        
        System.out.println("DBUtil: Attempting to connect to: " + URL);
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        System.out.println("DBUtil: Connection established successfully");
        return conn;
    }
}

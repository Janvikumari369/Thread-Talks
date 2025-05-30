package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/threadtalk";
    private static final String USER = "root";
    private static final String PASSWORD = "Deepanshu@779820";

    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Database connection successful!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("✗ MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("✗ Database connection failed!");
            System.out.println("Error: " + e.getMessage());
        }
    }
} 
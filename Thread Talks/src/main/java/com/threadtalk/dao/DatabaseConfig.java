package com.threadtalk.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    // The URL should match your Workbench connection:
    // - localhost is the hostname
    // - 3306 is the default MySQL port
    // - thread_talk is the database name you need to create
    private static final String URL = "jdbc:mysql://localhost:3306/thread_talk";
    
    // This should match your Workbench connection username (usually "root")
    private static final String USER = "root";
    
    // This should match the password you use to connect in Workbench
    private static final String PASSWORD = "Deepanshu@779820";

    public static Connection getConnection() throws SQLException {
        try {
            // Register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create database connection
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("ThreadTalk - Database Connection Test");
        System.out.println("------------------------------------");
        
        try {
            System.out.println("Attempting to connect to database...");
            Connection conn = getConnection();
            System.out.println("✓ Database connection successful!");
            System.out.println("Database URL: " + URL);
            System.out.println("Username: " + USER);
            conn.close();
            System.out.println("Connection closed successfully.");
        } catch (SQLException e) {
            System.out.println("✗ Database connection failed!");
            System.out.println("Error: " + e.getMessage());
            System.out.println("\nPlease check:");
            System.out.println("1. Is MySQL running? (Check MySQL80 service)");
            System.out.println("2. Is the password correct?");
            System.out.println("3. Does the threadtalk database exist?");
            System.out.println("\nTo create the database, run this SQL command:");
            System.out.println("CREATE DATABASE threadtalk;");
        }
    }
} 
package com.threadtalk.dao;

import com.threadtalk.model.User;
import com.threadtalk.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    password, // Will be hashed in User constructor
                    rs.getString("email")
                );
                if (user.verifyPassword(password)) {
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error authenticating user: " + username, e);
        }
        return null;
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    "", // Password not needed for this operation
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user: " + username, e);
        }
        return null;
    }

    public boolean addUser(String username, String password, String email) {
        String query = "INSERT INTO users (username, password, email, created_at) VALUES (?, ?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            User user = new User(0, username, password, email);
            stmt.setString(1, username);
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, email);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding user: " + username, e);
            return false;
        }
    }

    public boolean updateOnlineStatus(int userId, boolean isOnline) {
        String query = "UPDATE users SET is_online = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, isOnline);
            stmt.setInt(2, userId);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating online status for user ID: " + userId, e);
            return false;
        }
    }

    public List<User> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();
        String query = "SELECT * FROM users WHERE is_online = true";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                onlineUsers.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    "", // Password not needed for this operation
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting online users", e);
        }
        return onlineUsers;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    "", // Password not needed for this operation
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all users", e);
        }
        return users;
    }
} 
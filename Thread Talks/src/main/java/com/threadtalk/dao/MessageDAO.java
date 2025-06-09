package com.threadtalk.dao;

import com.threadtalk.model.Message;
import com.threadtalk.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MessageDAO {
    private static final Logger LOGGER = Logger.getLogger(MessageDAO.class.getName());

    public boolean addMessage(int senderId, String content) {
        String query = "INSERT INTO messages (sender_id, content) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, senderId);
            stmt.setString(2, content);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding message from sender ID: " + senderId, e);
            return false;
        }
    }

    public List<Message> getRecentMessages(int limit) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT m.*, u.username FROM messages m " +
                      "JOIN users u ON m.sender_id = u.id " +
                      "ORDER BY m.sent_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("id"),
                    rs.getInt("sender_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_at")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting recent messages", e);
        }
        return messages;
    }

    public List<Message> getMessagesByUser(int userId) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages WHERE sender_id = ? ORDER BY sent_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("id"),
                    rs.getInt("sender_id"),
                    rs.getString("content"),
                    rs.getTimestamp("sent_at")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting messages for user ID: " + userId, e);
        }
        return messages;
    }

    public boolean deleteMessage(int messageId) {
        String query = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, messageId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting message ID: " + messageId, e);
            return false;
        }
    }
} 
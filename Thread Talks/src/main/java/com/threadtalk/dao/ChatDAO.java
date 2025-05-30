package dao;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChatDAO {
    public static void insertMessage(String username, String message) {
        String sql = "INSERT INTO messages (username, message, timestamp) VALUES (?, ?, NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Failed to insert message into database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Database connection error: " + e.getMessage());
        }
    }
}
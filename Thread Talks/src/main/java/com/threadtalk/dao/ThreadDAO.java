package com.threadtalk.dao;

import com.threadtalk.model.Thread;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThreadDAO {
    public void addThread(Thread thread) throws SQLException {
        String sql = "INSERT INTO threads (title, content, user_id) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, thread.getTitle());
            pstmt.setString(2, thread.getContent());
            pstmt.setInt(3, thread.getUserId());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    thread.setThreadId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public Thread getThreadById(int threadId) throws SQLException {
        String sql = "SELECT t.*, u.username as author_username FROM threads t " +
                    "JOIN users u ON t.user_id = u.user_id " +
                    "WHERE t.thread_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threadId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Thread thread = new Thread();
                    thread.setThreadId(rs.getInt("thread_id"));
                    thread.setTitle(rs.getString("title"));
                    thread.setContent(rs.getString("content"));
                    thread.setUserId(rs.getInt("user_id"));
                    thread.setCreatedAt(rs.getString("created_at"));
                    thread.setUpdatedAt(rs.getString("updated_at"));
                    thread.setAuthorUsername(rs.getString("author_username"));
                    return thread;
                }
            }
        }
        return null;
    }
    
    public List<Thread> getAllThreads() throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String sql = "SELECT t.*, u.username as author_username FROM threads t " +
                    "JOIN users u ON t.user_id = u.user_id " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Thread thread = new Thread();
                thread.setThreadId(rs.getInt("thread_id"));
                thread.setTitle(rs.getString("title"));
                thread.setContent(rs.getString("content"));
                thread.setUserId(rs.getInt("user_id"));
                thread.setCreatedAt(rs.getString("created_at"));
                thread.setUpdatedAt(rs.getString("updated_at"));
                thread.setAuthorUsername(rs.getString("author_username"));
                threads.add(thread);
            }
        }
        return threads;
    }
    
    public List<Thread> getThreadsByUser(int userId) throws SQLException {
        List<Thread> threads = new ArrayList<>();
        String sql = "SELECT t.*, u.username as author_username FROM threads t " +
                    "JOIN users u ON t.user_id = u.user_id " +
                    "WHERE t.user_id = ? " +
                    "ORDER BY t.created_at DESC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Thread thread = new Thread();
                    thread.setThreadId(rs.getInt("thread_id"));
                    thread.setTitle(rs.getString("title"));
                    thread.setContent(rs.getString("content"));
                    thread.setUserId(rs.getInt("user_id"));
                    thread.setCreatedAt(rs.getString("created_at"));
                    thread.setUpdatedAt(rs.getString("updated_at"));
                    thread.setAuthorUsername(rs.getString("author_username"));
                    threads.add(thread);
                }
            }
        }
        return threads;
    }
    
    public void updateThread(Thread thread) throws SQLException {
        String sql = "UPDATE threads SET title = ?, content = ? WHERE thread_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, thread.getTitle());
            pstmt.setString(2, thread.getContent());
            pstmt.setInt(3, thread.getThreadId());
            pstmt.setInt(4, thread.getUserId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteThread(int threadId, int userId) throws SQLException {
        String sql = "DELETE FROM threads WHERE thread_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threadId);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
        }
    }
} 
package com.threadtalk.dao;

import com.threadtalk.model.Comment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {
    public void addComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO comments (content, thread_id, user_id, parent_comment_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getThreadId());
            pstmt.setInt(3, comment.getUserId());
            if (comment.getParentCommentId() != null) {
                pstmt.setInt(4, comment.getParentCommentId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setCommentId(generatedKeys.getInt(1));
                }
            }
        }
    }
    
    public Comment getCommentById(int commentId) throws SQLException {
        String sql = "SELECT c.*, u.username as author_username FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.comment_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, commentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(rs.getInt("comment_id"));
                    comment.setContent(rs.getString("content"));
                    comment.setThreadId(rs.getInt("thread_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setParentCommentId(rs.getObject("parent_comment_id", Integer.class));
                    comment.setCreatedAt(rs.getString("created_at"));
                    comment.setUpdatedAt(rs.getString("updated_at"));
                    comment.setAuthorUsername(rs.getString("author_username"));
                    return comment;
                }
            }
        }
        return null;
    }
    
    public List<Comment> getCommentsByThread(int threadId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.*, u.username as author_username FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.thread_id = ? " +
                    "ORDER BY c.created_at ASC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, threadId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(rs.getInt("comment_id"));
                    comment.setContent(rs.getString("content"));
                    comment.setThreadId(rs.getInt("thread_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setParentCommentId(rs.getObject("parent_comment_id", Integer.class));
                    comment.setCreatedAt(rs.getString("created_at"));
                    comment.setUpdatedAt(rs.getString("updated_at"));
                    comment.setAuthorUsername(rs.getString("author_username"));
                    comments.add(comment);
                }
            }
        }
        return comments;
    }
    
    public List<Comment> getRepliesByComment(int parentCommentId) throws SQLException {
        List<Comment> replies = new ArrayList<>();
        String sql = "SELECT c.*, u.username as author_username FROM comments c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.parent_comment_id = ? " +
                    "ORDER BY c.created_at ASC";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, parentCommentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(rs.getInt("comment_id"));
                    comment.setContent(rs.getString("content"));
                    comment.setThreadId(rs.getInt("thread_id"));
                    comment.setUserId(rs.getInt("user_id"));
                    comment.setParentCommentId(rs.getObject("parent_comment_id", Integer.class));
                    comment.setCreatedAt(rs.getString("created_at"));
                    comment.setUpdatedAt(rs.getString("updated_at"));
                    comment.setAuthorUsername(rs.getString("author_username"));
                    replies.add(comment);
                }
            }
        }
        return replies;
    }
    
    public void updateComment(Comment comment) throws SQLException {
        String sql = "UPDATE comments SET content = ? WHERE comment_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, comment.getContent());
            pstmt.setInt(2, comment.getCommentId());
            pstmt.setInt(3, comment.getUserId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteComment(int commentId, int userId) throws SQLException {
        String sql = "DELETE FROM comments WHERE comment_id = ? AND user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, commentId);
            pstmt.setInt(2, userId);
            
            pstmt.executeUpdate();
        }
    }
} 
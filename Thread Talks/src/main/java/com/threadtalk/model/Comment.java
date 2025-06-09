package com.threadtalk.model;

public class Comment {
    private int commentId;
    private String content;
    private int threadId;
    private int userId;
    private Integer parentCommentId;
    private String createdAt;
    private String updatedAt;
    private String authorUsername; // To store the username of the commenter

    public Comment() {}

    public Comment(String content, int threadId, int userId, Integer parentCommentId) {
        this.content = content;
        this.threadId = threadId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
    }

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getThreadId() { return threadId; }
    public void setThreadId(int threadId) { this.threadId = threadId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Integer getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Integer parentCommentId) { this.parentCommentId = parentCommentId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
} 
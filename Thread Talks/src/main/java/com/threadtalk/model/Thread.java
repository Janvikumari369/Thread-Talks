package com.threadtalk.model;

public class Thread {
    private int threadId;
    private String title;
    private String content;
    private int userId;
    private String createdAt;
    private String updatedAt;
    private String authorUsername; // To store the username of the thread creator

    public Thread() {}

    public Thread(String title, String content, int userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    // Getters and Setters
    public int getThreadId() { return threadId; }
    public void setThreadId(int threadId) { this.threadId = threadId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }
} 
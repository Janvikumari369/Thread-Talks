package com.threadtalk.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private Date createdAt;
    private boolean isOnline;

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.email = email;
        this.createdAt = new Date();
        this.isOnline = false;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean verifyPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", isOnline=" + isOnline +
                '}';
    }
} 
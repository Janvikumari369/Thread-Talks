package com.threadtalk.model;

import java.util.Date;

public class Message {
    private int id;
    private int senderId;
    private String content;
    private Date sentAt;

    public Message(int id, int senderId, String content, Date sentAt) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", content='" + content + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
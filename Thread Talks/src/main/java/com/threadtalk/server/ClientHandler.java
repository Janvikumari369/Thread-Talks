package com.threadtalk.server;

import com.threadtalk.dao.MessageDAO;
import com.threadtalk.model.User;
import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private static final int MAX_MESSAGE_LENGTH = 1000;

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;
    private User user;
    private ChatServer server;
    private MessageDAO messageDAO;
    private boolean isRunning;

    public ClientHandler(Socket socket, String username, ChatServer server) {
        this.clientSocket = socket;
        this.username = username;
        this.server = server;
        this.messageDAO = new MessageDAO();
        this.isRunning = false;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing client handler", e);
        }
    }

    public void start() {
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                String message = in.readLine();
                if (message == null) {
                    break;
                }

                if (message.startsWith("MSG:")) {
                    String content = message.substring(4);
                    if (isValidMessage(content)) {
                        String sanitizedContent = sanitizeMessage(content);
                        if (messageDAO.addMessage(user.getId(), sanitizedContent)) {
                            server.broadcastMessage(username, sanitizedContent);
                        } else {
                            sendMessage("ERROR:Failed to save message");
                        }
                    } else {
                        sendMessage("ERROR:Message too long or empty");
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error handling client messages", e);
        } finally {
            cleanup();
        }
    }

    private boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty() && message.length() <= MAX_MESSAGE_LENGTH;
    }

    private String sanitizeMessage(String message) {
        // Basic XSS prevention
        return message.replace("<", "&lt;").replace(">", "&gt;");
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void cleanup() {
        isRunning = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error cleaning up client resources", e);
        }
        server.removeClient(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
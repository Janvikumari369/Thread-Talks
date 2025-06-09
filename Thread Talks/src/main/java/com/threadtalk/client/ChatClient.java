package com.threadtalk.client;

import com.threadtalk.ui.LoginDialog;
import com.threadtalk.ui.ChatDashboardUI;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Arrays;
import java.util.List;

public class ChatClient {
    private static final Logger LOGGER = Logger.getLogger(ChatClient.class.getName());
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final int RECONNECT_DELAY_MS = 2000;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String password;
    private ChatDashboardUI dashboard;
    private boolean isConnected = false;

    public ChatClient() {
        dashboard = new ChatDashboardUI(this);
    }

    public boolean connect() {
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            try {
                // Show login dialog
                LoginDialog loginDialog = new LoginDialog(dashboard);
                loginDialog.setVisible(true);

                if (!loginDialog.isLoginSuccessful()) {
                    return false;
                }

                username = loginDialog.getUsername();
                password = loginDialog.getPassword();

                // Connect to server
                socket = new Socket("localhost", 5000);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Send authentication request
                out.println("AUTH:" + username + ":" + password);

                // Wait for authentication response
                String response = in.readLine();
                if (response != null && response.startsWith("AUTH_SUCCESS")) {
                    isConnected = true;
                    dashboard.setTitle("ThreadTalk - " + username);
                    dashboard.setVisible(true);
                    startMessageListener();
                    return true;
                } else {
                    JOptionPane.showMessageDialog(dashboard,
                        "Authentication failed. Please check your credentials.",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                    closeConnection();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Connection attempt " + (attempts + 1) + " failed", e);
                attempts++;
                if (attempts < MAX_RECONNECT_ATTEMPTS) {
                    try {
                        Thread.sleep(RECONNECT_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(dashboard,
            "Failed to connect after " + MAX_RECONNECT_ATTEMPTS + " attempts.",
            "Connection Error",
            JOptionPane.ERROR_MESSAGE);
        return false;
    }

    private void startMessageListener() {
        new Thread(() -> {
            try {
                String message;
                while (isConnected && (message = in.readLine()) != null) {
                    if (message.startsWith("MSG:")) {
                        String[] parts = message.substring(4).split(":", 2);
                        if (parts.length == 2) {
                            dashboard.addMessage(parts[0], parts[1]);
                        }
                    } else if (message.startsWith("USER_LIST:")) {
                        String[] users = message.substring(10).split(" ");
                        dashboard.updateUserList(Arrays.asList(users));
                    } else if (message.equals("SERVER_SHUTDOWN")) {
                        handleServerShutdown();
                    }
                }
            } catch (IOException e) {
                if (isConnected) {
                    LOGGER.log(Level.SEVERE, "Error reading from server", e);
                    handleConnectionLoss();
                }
            }
        }).start();
    }

    public void sendMessage(String message) {
        if (isConnected && out != null) {
            out.println("MSG:" + message);
        }
    }

    public void disconnect() {
        isConnected = false;
        closeConnection();
        dashboard.dispose();
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing connection", e);
        }
    }

    private void handleConnectionLoss() {
        isConnected = false;
        closeConnection();
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(dashboard,
                "Connection to server lost. Please restart the application.",
                "Connection Lost",
                JOptionPane.ERROR_MESSAGE);
            dashboard.dispose();
        });
    }

    private void handleServerShutdown() {
        isConnected = false;
        closeConnection();
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(dashboard,
                "Server is shutting down. Please try again later.",
                "Server Shutdown",
                JOptionPane.INFORMATION_MESSAGE);
            dashboard.dispose();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClient client = new ChatClient();
            client.connect();
        });
    }
}
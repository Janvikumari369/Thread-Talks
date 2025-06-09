package com.threadtalk.server;

import com.threadtalk.dao.UserDAO;
import com.threadtalk.model.User;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ChatServer {
    private static final Logger LOGGER = Logger.getLogger(ChatServer.class.getName());
    private static final int PORT = 5000;
    private static final int MAX_CLIENTS = 100;

    private ServerSocket serverSocket;
    private ExecutorService clientThreadPool;
    private Map<String, ClientHandler> clients;
    private UserDAO userDAO;
    private boolean isRunning;

    public ChatServer() {
        clientThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        clients = new ConcurrentHashMap<>();
        userDAO = new UserDAO();
        isRunning = false;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            LOGGER.info("Server started on port " + PORT);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("New client connected: " + clientSocket.getInetAddress());
                    handleNewClient(clientSocket);
                } catch (IOException e) {
                    if (isRunning) {
                        LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Server failed to start", e);
        } finally {
            shutdown();
        }
    }

    private void handleNewClient(Socket clientSocket) {
        clientThreadPool.execute(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Wait for authentication request
                String authRequest = in.readLine();
                if (authRequest != null && authRequest.startsWith("AUTH:")) {
                    String[] parts = authRequest.substring(5).split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String password = parts[1];

                        // Authenticate user
                        User user = userDAO.authenticateUser(username, password);
                        if (user != null) {
                            // Check if user is already connected
                            if (clients.containsKey(username)) {
                                out.println("AUTH_ERROR:User already connected");
                                clientSocket.close();
                                return;
                            }

                            // Create client handler
                            ClientHandler clientHandler = new ClientHandler(clientSocket, username, this);
                            clientHandler.setUser(user);
                            clients.put(username, clientHandler);
                            out.println("AUTH_SUCCESS");
                            clientHandler.start();

                            // Update user's online status
                            userDAO.updateOnlineStatus(user.getId(), true);
                            broadcastUserList();
                        } else {
                            out.println("AUTH_ERROR:Invalid credentials");
                            clientSocket.close();
                        }
                    } else {
                        out.println("AUTH_ERROR:Invalid authentication format");
                        clientSocket.close();
                    }
                } else {
                    out.println("AUTH_ERROR:Authentication required");
                    clientSocket.close();
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error handling new client", e);
                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
            }
        });
    }

    public void broadcastMessage(String sender, String message) {
        String formattedMessage = "MSG:" + sender + ":" + message;
        for (ClientHandler client : clients.values()) {
            client.sendMessage(formattedMessage);
        }
    }

    public void removeClient(String username) {
        clients.remove(username);
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            userDAO.updateOnlineStatus(user.getId(), false);
        }
        broadcastUserList();
    }

    private void broadcastUserList() {
        List<String> onlineUsers = new ArrayList<>(clients.keySet());
        String userListMessage = "USER_LIST:" + String.join(" ", onlineUsers);
        for (ClientHandler client : clients.values()) {
            client.sendMessage(userListMessage);
        }
    }

    public void shutdown() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing server socket", e);
        }

        // Notify all clients
        for (ClientHandler client : clients.values()) {
            client.sendMessage("SERVER_SHUTDOWN");
        }

        // Shutdown thread pool
        clientThreadPool.shutdown();
        try {
            if (!clientThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                clientThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            clientThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.start();
    }
}
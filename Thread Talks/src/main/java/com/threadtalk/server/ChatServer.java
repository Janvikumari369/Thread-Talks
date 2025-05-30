package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    static Set<ClientHandler> clientHandlers = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("✅ Chat server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    public static void broadcast(String message, ClientHandler excludeHandler) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                if (handler != excludeHandler) {
                    handler.sendMessage(message);
                }
            }
        }
    }

    public static void updateUsers() {
        StringBuilder sb = new StringBuilder("USER_LIST:");
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers) {
                sb.append(handler.getUsername()).append(" ");
            }
            for (ClientHandler handler : clientHandlers) {
                handler.sendMessage(sb.toString().trim());
            }
        }
    }

    public static void removeClient(ClientHandler handler) {
        clientHandlers.remove(handler);
        updateUsers();
    }
}
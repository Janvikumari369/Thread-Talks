package server;

import dao.ChatDAO;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            this.username = in.readLine();
            ChatServer.updateUsers();

            String message;
            while ((message = in.readLine()) != null) {
                ChatDAO.insertMessage(username, message);
                ChatServer.broadcast(username + ": " + message, this);
            }

        } catch (IOException e) {
            System.err.println("❌ Client disconnected: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            ChatServer.removeClient(this);
        }
    }
}
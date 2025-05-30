package client;

import ui.ChatDashboardUI;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String username = JOptionPane.showInputDialog("Enter your username:");
            out.println(username);

            ChatDashboardUI dashboard = new ChatDashboardUI(out, username);
            dashboard.setVisible(true);

            new Thread(() -> {
                try {
                    String serverMsg;
                    while ((serverMsg = in.readLine()) != null) {
                        if (serverMsg.startsWith("USER_LIST:")) {
                            String[] users = serverMsg.substring(10).split(" ");
                            dashboard.updateUserList(Arrays.asList(users));
                        } else {
                            dashboard.appendMessage(serverMsg);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
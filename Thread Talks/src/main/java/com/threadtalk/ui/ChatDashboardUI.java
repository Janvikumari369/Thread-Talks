package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintWriter;
import java.util.List;

public class ChatDashboardUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private PrintWriter out;

    public ChatDashboardUI(PrintWriter out, String username) {
        this.out = out;

        setTitle("ThreadTalk - Chat Dashboard [" + username + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Chat Area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // Message input
        JPanel inputPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // Online users
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("Online Users", JLabel.CENTER), BorderLayout.NORTH);
        userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);
        add(userPanel, BorderLayout.EAST);

        // Action
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.setText("");
        }
    }

    public void appendMessage(String msg) {
        chatArea.append(msg + "\n");
    }

    public void updateUserList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            for (String user : users) {
                userListModel.addElement(user);
            }
        });
    }
}
package com.threadtalk.ui;

import com.threadtalk.client.ChatClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChatDashboardUI extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    private ChatClient client;

    public ChatDashboardUI(ChatClient client) {
        this.client = client;
        initializeComponents();
        layoutComponents();
        addEventListeners();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        messageField = new JTextField();
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setPreferredSize(new Dimension(150, 0));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Chat area with scroll pane
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Message input panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.add(messageField, BorderLayout.CENTER);
        JButton sendButton = new JButton("Send");
        messagePanel.add(sendButton, BorderLayout.EAST);

        // User list with scroll pane and title
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("Online Users", JLabel.CENTER), BorderLayout.NORTH);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        userPanel.add(userScrollPane, BorderLayout.CENTER);

        // Split pane for chat and user list
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatScrollPane, userPanel);
        splitPane.setDividerLocation(600);

        add(splitPane, BorderLayout.CENTER);
        add(messagePanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        messageField.addActionListener(e -> sendMessage());
        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnect();
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);
            messageField.setText("");
        }
    }

    public void addMessage(String sender, String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(sender + ": " + message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
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
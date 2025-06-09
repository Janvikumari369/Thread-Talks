package com.threadtalk.view;

import com.threadtalk.dao.UserDAO;
import com.threadtalk.model.User;
import javax.swing.*;
import java.awt.*;

public class ThreadTalkGUI extends JFrame {
    private final UserDAO userDAO;
    private User currentUser;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public ThreadTalkGUI() {
        this.userDAO = new UserDAO();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("ThreadTalk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);

        // Create login panel
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "login");

        // Show login panel
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel titleLabel = new JLabel("ThreadTalk - Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        // Register section
        JLabel registerLabel = new JLabel("New user? Register here:");
        gbc.gridy = 4;
        panel.add(registerLabel, gbc);

        // Email field for registration
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(emailField, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(registerButton, gbc);

        // Add action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegistration());

        return panel;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password");
            return;
        }

        User user = userDAO.authenticateUser(username, password);
        if (user != null) {
            currentUser = user;
            JOptionPane.showMessageDialog(this, "Login successful!");
            // TODO: Show main forum interface
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }
    }

    private void handleRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields");
            return;
        }

        User existingUser = userDAO.getUserByUsername(username);
        if (existingUser != null) {
            JOptionPane.showMessageDialog(this, "Username already exists");
            return;
        }

        if (userDAO.addUser(username, password, email)) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            
            // Clear fields
            usernameField.setText("");
            passwordField.setText("");
            emailField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ThreadTalkGUI().setVisible(true);
        });
    }
} 
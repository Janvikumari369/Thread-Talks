package com.threadtalk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private boolean loginSuccessful = false;
    private String username;
    private String password;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        initializeComponents();
        layoutComponents();
        addEventListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        loginButton.addActionListener(e -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter both username and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            loginSuccessful = true;
            dispose();
        });

        registerButton.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            RegisterDialog registerDialog = new RegisterDialog(parentFrame);
            registerDialog.setVisible(true);

            if (registerDialog.isRegistrationSuccessful()) {
                // Auto-fill login fields with registration data
                usernameField.setText(registerDialog.getUsername());
                passwordField.setText(registerDialog.getPassword());
                
                JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login with your new credentials.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add enter key listener
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
} 
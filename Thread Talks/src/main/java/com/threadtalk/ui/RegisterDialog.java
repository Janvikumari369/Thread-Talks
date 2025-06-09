package com.threadtalk.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton cancelButton;
    private boolean registrationSuccessful = false;
    private String username;
    private String password;
    private String email;

    public RegisterDialog(Frame parent) {
        super(parent, "Register", true);
        initializeComponents();
        layoutComponents();
        addEventListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        emailField = new JTextField(20);
        registerButton = new JButton("Register");
        cancelButton = new JButton("Cancel");
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

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(emailField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addEventListeners() {
        registerButton.addActionListener(e -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            email = emailField.getText().trim();

            // Validate input
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            registrationSuccessful = true;
            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        // Add enter key listener
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registerButton.doClick();
                }
            }
        };
        usernameField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
        confirmPasswordField.addKeyListener(enterListener);
        emailField.addKeyListener(enterListener);
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public boolean isRegistrationSuccessful() {
        return registrationSuccessful;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
} 
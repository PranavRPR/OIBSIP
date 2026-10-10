package com.oibsip.reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final Database db;
    private final JTextField userField = new UITheme.TextInput(18);
    private final JPasswordField passField = new UITheme.PasswordInput(18);

    public LoginFrame(Database db) {
        this.db = db;
        setTitle("Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(560, 640);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(520, 600));

        UITheme.GradientPanel root = new UITheme.GradientPanel();
        root.setLayout(new GridBagLayout());

        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 480));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 0, 6, 0);

        JLabel brand = new JLabel("RESERVATION");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 13));
        brand.setForeground(UITheme.PRIMARY);
        card.add(brand, g);

        g.gridy++;
        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        card.add(title, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 40, 0);
        JLabel sub = new JLabel("Sign in to manage your tickets");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sub.setForeground(UITheme.TEXT_SOFT);
        card.add(sub, g);

        g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(6, 0, 6, 0);

        g.gridy++;
        card.add(UITheme.label("USERNAME"), g);

        g.gridy++;
        g.insets = new Insets(0, 0, 18, 0);
        card.add(userField, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 6, 0);
        card.add(UITheme.label("PASSWORD"), g);

        g.gridy++;
        g.insets = new Insets(0, 0, 28, 0);
        card.add(passField, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 12, 0);
        UITheme.PrimaryButton loginBtn = new UITheme.PrimaryButton("Sign In");
        loginBtn.setPreferredSize(new Dimension(200, 46));
        card.add(loginBtn, g);

        g.gridy++;
        g.insets = new Insets(0, 0, 0, 0);
        JLabel hint = new JLabel("Default:  admin  /  admin123");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(UITheme.TEXT_SOFT);
        g.anchor = GridBagConstraints.CENTER;
        card.add(hint, g);

        loginBtn.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginBtn);

        root.add(card);
        setContentPane(root);
    }

    private void doLogin() {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            if (db.authenticate(u, p)) {
                dispose();
                new ReservationFrame(db).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Access denied. Invalid credentials.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
                passField.requestFocus();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
}
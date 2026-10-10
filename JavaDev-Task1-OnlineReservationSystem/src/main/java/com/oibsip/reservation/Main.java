package com.oibsip.reservation;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Database db = new Database();
                new LoginFrame(db).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Startup error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

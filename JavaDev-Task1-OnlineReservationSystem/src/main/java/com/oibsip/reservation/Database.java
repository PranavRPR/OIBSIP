package com.oibsip.reservation;

import java.sql.*;
import java.util.*;

public class Database {
    private static final String URL = "jdbc:sqlite:reservation.db";
    private final Connection conn;

    public Database() throws SQLException {
        conn = DriverManager.getConnection(URL);
        init();
    }

    private void init() throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, password TEXT NOT NULL)");
            st.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                    "pnr TEXT PRIMARY KEY, passenger_name TEXT NOT NULL, " +
                    "train_number TEXT NOT NULL, train_name TEXT NOT NULL, " +
                    "class_type TEXT NOT NULL, journey_date TEXT NOT NULL, " +
                    "source TEXT NOT NULL, destination TEXT NOT NULL)");
        }
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT OR IGNORE INTO users(username,password) VALUES(?,?)")) {
            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.executeUpdate();
        }
    }

    public boolean authenticate(String u, String p) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM users WHERE username=? AND password=?")) {
            ps.setString(1, u);
            ps.setString(2, p);
            return ps.executeQuery().next();
        }
    }

    public String insertReservation(Map<String, String> r) throws SQLException {
        String pnr = generatePnr();
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reservations(pnr,passenger_name,train_number,train_name," +
                "class_type,journey_date,source,destination) VALUES(?,?,?,?,?,?,?,?)")) {
            ps.setString(1, pnr);
            ps.setString(2, r.get("passenger_name"));
            ps.setString(3, r.get("train_number"));
            ps.setString(4, r.get("train_name"));
            ps.setString(5, r.get("class_type"));
            ps.setString(6, r.get("journey_date"));
            ps.setString(7, r.get("source"));
            ps.setString(8, r.get("destination"));
            ps.executeUpdate();
        }
        return pnr;
    }

    public Map<String, String> getReservation(String pnr) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM reservations WHERE pnr=?")) {
            ps.setString(1, pnr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("pnr", rs.getString("pnr"));
                m.put("passenger_name", rs.getString("passenger_name"));
                m.put("train_number", rs.getString("train_number"));
                m.put("train_name", rs.getString("train_name"));
                m.put("class_type", rs.getString("class_type"));
                m.put("journey_date", rs.getString("journey_date"));
                m.put("source", rs.getString("source"));
                m.put("destination", rs.getString("destination"));
                return m;
            }
        }
        return null;
    }

    public boolean deleteReservation(String pnr) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM reservations WHERE pnr=?")) {
            ps.setString(1, pnr);
            return ps.executeUpdate() > 0;
        }
    }

    private String generatePnr() {
        return "PNR" + (System.currentTimeMillis() % 100_000_000L)
                + String.format("%03d", (int) (Math.random() * 1000));
    }

    public void close() throws SQLException {
        if (conn != null) conn.close();
    }
}

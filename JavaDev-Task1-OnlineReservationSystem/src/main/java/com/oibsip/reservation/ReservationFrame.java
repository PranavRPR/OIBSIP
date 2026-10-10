package com.oibsip.reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReservationFrame extends JFrame {
    private final Database db;

    private final UITheme.TextInput nameField = new UITheme.TextInput(15);
    private final JComboBox<String> trainNumberBox = new JComboBox<>();
    private final UITheme.TextInput trainNameField = new UITheme.TextInput(15);
    private final JComboBox<String> classBox = new JComboBox<>(
            new String[]{"Sleeper", "AC 3-Tier", "AC 2-Tier", "AC First Class", "General"});
    private final UITheme.TextInput dateField = new UITheme.TextInput(15);
    private final UITheme.TextInput sourceField = new UITheme.TextInput(15);
    private final UITheme.TextInput destField = new UITheme.TextInput(15);

    private final UITheme.TextInput pnrField = new UITheme.TextInput(15);
    private final JTextArea detailsArea = new JTextArea(12, 40);

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);

    public ReservationFrame(Database db) {
        this.db = db;
        setTitle("Reservation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 860);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 720));

        UITheme.GradientPanel root = new UITheme.GradientPanel();
        root.setLayout(new BorderLayout());
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildTabs(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(28, 60, 8, 60));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel brand = new JLabel("RESERVATION SYSTEM");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 11));
        brand.setForeground(UITheme.PRIMARY);

        JLabel title = new JLabel("Train Ticketing");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        left.add(brand);
        left.add(Box.createVerticalStrut(4));
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JLabel user = new JLabel("admin");
        user.setOpaque(true);
        user.setBackground(new Color(0x334155));
        user.setForeground(Color.WHITE);
        user.setFont(new Font("Segoe UI", Font.BOLD, 12));
        user.setBorder(new EmptyBorder(8, 14, 8, 14));

        UITheme.PrimaryButton logout = new UITheme.PrimaryButton(
                "Sign Out", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        logout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                    "Sign out and return to login?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame(db).setVisible(true);
            }
        });

        right.add(user);
        right.add(logout);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JComponent buildTabs() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 18));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(18, 60, 40, 60));

        JPanel tabBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tabBar.setOpaque(false);

        UITheme.PrimaryButton bookTab = new UITheme.PrimaryButton("  Book Ticket  ");
        UITheme.PrimaryButton cancelTab = new UITheme.PrimaryButton(
                "  Cancel Ticket  ", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);

        bookTab.addActionListener(e -> cards.show(content, "book"));
        cancelTab.addActionListener(e -> cards.show(content, "cancel"));

        tabBar.add(bookTab);
        tabBar.add(cancelTab);

        content.setOpaque(false);
        content.add(wrapCard(buildBookingContent()), "book");
        content.add(wrapCard(buildCancelContent()), "cancel");
        cards.show(content, "book");

        wrapper.add(tabBar, BorderLayout.NORTH);
        wrapper.add(content, BorderLayout.CENTER);
        return wrapper;
    }

    /** Stretch card to fill the available space. */
    private JComponent wrapCard(JComponent inner) {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);

        UITheme.CardPanel card = new UITheme.CardPanel(20);
        card.setLayout(new BorderLayout());
        card.add(inner, BorderLayout.CENTER);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0;
        g.weightx = 1.0; g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 0, 0, 0);
        holder.add(card, g);

        return holder;
    }

    private JComponent buildBookingContent() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints g = new GridBagConstraints();

        for (String n : TrainData.numbers()) trainNumberBox.addItem(n);
        UITheme.styleCombo(trainNumberBox);
        UITheme.styleCombo(classBox);
        trainNumberBox.setPreferredSize(new Dimension(280, 42));
        classBox.setPreferredSize(new Dimension(280, 42));

        trainNameField.setEditable(false);
        trainNameField.setForeground(UITheme.TEXT_SOFT);
        trainNameField.setText(TrainData.getName((String) trainNumberBox.getSelectedItem()));
        trainNumberBox.addActionListener(e ->
                trainNameField.setText(TrainData.getName((String) trainNumberBox.getSelectedItem())));

        dateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Heading
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(4, 8, 26, 8);
        grid.add(UITheme.heading("Book a New Ticket"), g);

        g.gridwidth = 1;

        addRow(grid, g, 1, "PASSENGER NAME", nameField);
        addRow(grid, g, 2, "TRAIN NUMBER", trainNumberBox);
        addRow(grid, g, 3, "TRAIN NAME (AUTO)", trainNameField);
        addRow(grid, g, 4, "CLASS TYPE", classBox);
        addRow(grid, g, 5, "DATE OF JOURNEY  (yyyy-MM-dd)", dateField);
        addRow(grid, g, 6, "SOURCE STATION", sourceField);
        addRow(grid, g, 7, "DESTINATION STATION", destField);

        // Spacer — absorbs extra vertical space so button sits at bottom
        g.gridx = 0; g.gridy = 8; g.gridwidth = 2;
        g.weightx = 1.0; g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(0, 0, 0, 0);
        grid.add(Box.createGlue(), g);

        // Button row
        g.gridx = 0; g.gridy = 9; g.gridwidth = 2;
        g.weightx = 0.0; g.weighty = 0.0;
        g.anchor = GridBagConstraints.EAST;
        g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(18, 8, 4, 8);

        UITheme.PrimaryButton bookBtn = new UITheme.PrimaryButton("Book Ticket");
        bookBtn.setPreferredSize(new Dimension(200, 46));
        bookBtn.setMinimumSize(new Dimension(200, 46));
        bookBtn.addActionListener(e -> bookTicket());
        grid.add(bookBtn, g);

        return grid;
    }

    private JComponent buildCancelContent() {
        JPanel p = new JPanel(new BorderLayout(0, 18));
        p.setOpaque(false);

        JLabel heading = UITheme.heading("Cancel a Ticket");
        p.add(heading, BorderLayout.NORTH);

        JPanel mid = new JPanel(new BorderLayout(0, 18));
        mid.setOpaque(false);

        JPanel searchBox = new JPanel(new BorderLayout(0, 6));
        searchBox.setOpaque(false);
        searchBox.add(UITheme.label("PNR NUMBER"), BorderLayout.NORTH);

        JPanel searchRow = new JPanel(new BorderLayout(12, 0));
        searchRow.setOpaque(false);
        searchRow.add(pnrField, BorderLayout.CENTER);

        UITheme.PrimaryButton fetchBtn = new UITheme.PrimaryButton(
                "Fetch", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        fetchBtn.setPreferredSize(new Dimension(120, 42));
        fetchBtn.setMinimumSize(new Dimension(120, 42));
        fetchBtn.addActionListener(e -> fetchPnr());
        searchRow.add(fetchBtn, BorderLayout.EAST);

        searchBox.add(searchRow, BorderLayout.CENTER);
        mid.add(searchBox, BorderLayout.NORTH);

        detailsArea.setEditable(false);
        detailsArea.setFont(UITheme.F_MONO);
        detailsArea.setBackground(UITheme.FIELD_BG);
        detailsArea.setForeground(UITheme.TEXT);
        detailsArea.setBorder(new EmptyBorder(18, 20, 18, 20));
        detailsArea.setText("No PNR fetched yet.\n\nEnter a PNR above and click Fetch to view booking details.");

        JScrollPane scroll = new JScrollPane(detailsArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        scroll.getViewport().setBackground(UITheme.FIELD_BG);
        mid.add(scroll, BorderLayout.CENTER);

        p.add(mid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottom.setOpaque(false);
        UITheme.PrimaryButton cancelBtn = new UITheme.PrimaryButton(
                "Confirm Cancellation", UITheme.DANGER, UITheme.DANGER_HOV, Color.WHITE);
        cancelBtn.setPreferredSize(new Dimension(240, 46));
        cancelBtn.setMinimumSize(new Dimension(240, 46));
        cancelBtn.addActionListener(e -> cancelTicket());
        bottom.add(cancelBtn);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    /** Label column fixed at 220px, field column stretches. */
    private void addRow(JPanel p, GridBagConstraints g, int y, String label, JComponent field) {
        g.gridx = 0; g.gridy = y;
        g.weightx = 0.0; g.weighty = 0.0;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 8, 8, 24);

        JLabel lbl = UITheme.label(label);
        lbl.setPreferredSize(new Dimension(220, 22));
        p.add(lbl, g);

        g.gridx = 1;
        g.weightx = 1.0;
        g.insets = new Insets(8, 0, 8, 8);
        p.add(field, g);
    }

    private void bookTicket() {
        String name = nameField.getText().trim();
        String trainNo = (String) trainNumberBox.getSelectedItem();
        String trainName = trainNameField.getText().trim();
        String cls = (String) classBox.getSelectedItem();
        String date = dateField.getText().trim();
        String src = sourceField.getText().trim();
        String dst = destField.getText().trim();

        if (name.isEmpty() || src.isEmpty() || dst.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (trainNo == null || !trainNo.matches("[0-9]+")) {
            JOptionPane.showMessageDialog(this, "Train number must be numeric.");
            return;
        }
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Date must be in yyyy-MM-dd format.");
            return;
        }

        try {
            Map<String, String> r = new LinkedHashMap<>();
            r.put("passenger_name", name);
            r.put("train_number", trainNo);
            r.put("train_name", trainName);
            r.put("class_type", cls);
            r.put("journey_date", date);
            r.put("source", src);
            r.put("destination", dst);

            String pnr = db.insertReservation(r);
            String msg = "Booking Confirmed!\n\n"
                    + "PNR: " + pnr + "\n"
                    + "Passenger: " + name + "\n"
                    + "Train: " + trainNo + " - " + trainName + "\n"
                    + "Class: " + cls + "\n"
                    + "Date: " + date + "\n"
                    + "From: " + src + "\n"
                    + "To: " + dst;
            JOptionPane.showMessageDialog(this, msg, "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);

            nameField.setText("");
            sourceField.setText("");
            destField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Booking failed: " + ex.getMessage());
        }
    }

    private void fetchPnr() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a PNR.");
            return;
        }
        try {
            Map<String, String> r = db.getReservation(pnr);
            if (r == null) {
                detailsArea.setText("No booking found for PNR: " + pnr);
                return;
            }
            detailsArea.setText(
                    "  PNR             : " + r.get("pnr") + "\n" +
                    "  Passenger       : " + r.get("passenger_name") + "\n" +
                    "  Train           : " + r.get("train_number") + " - " + r.get("train_name") + "\n" +
                    "  Class           : " + r.get("class_type") + "\n" +
                    "  Journey Date    : " + r.get("journey_date") + "\n" +
                    "  From            : " + r.get("source") + "\n" +
                    "  To              : " + r.get("destination") + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fetch failed: " + ex.getMessage());
        }
    }

    private void cancelTicket() {
        String pnr = pnrField.getText().trim();
        if (pnr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fetch a PNR first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel PNR " + pnr + "?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            if (db.deleteReservation(pnr)) {
                JOptionPane.showMessageDialog(this, "Cancellation successful.");
                detailsArea.setText("Cancellation complete. PNR " + pnr + " has been removed.");
                pnrField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "PNR not found.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Cancellation failed: " + ex.getMessage());
        }
    }
}
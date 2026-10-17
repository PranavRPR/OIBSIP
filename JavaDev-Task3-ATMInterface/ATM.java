import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ATM extends JFrame {
    private final Bank bank;
    private Account currentAccount;
    private final CardLayout cards = new CardLayout();
    private final JPanel mainPanel = new JPanel(cards);

    private JLabel balanceLabel;
    private JTextArea historyArea;
    private JLabel menuFeedback;

    // Login Inputs
    private UITheme.TextInput userIdField;
    private UITheme.PasswordInput pinField;

    public ATM(Bank bank) {
        this.bank = bank;
        setTitle("Java ATM");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        UITheme.GradientPanel root = new UITheme.GradientPanel();
        root.setLayout(new GridBagLayout());

        mainPanel.setOpaque(false);
        mainPanel.add(buildLoginPanel(), "login");
        mainPanel.add(buildMenuPanel(), "menu");
        mainPanel.add(buildActionPanel("Withdraw Amount", "withdraw"), "withdraw");
        mainPanel.add(buildActionPanel("Deposit Amount", "deposit"), "deposit");
        mainPanel.add(buildActionPanel("Transfer Funds", "transfer"), "transfer");
        mainPanel.add(buildHistoryPanel(), "history");

        root.add(mainPanel);
        setContentPane(root);
    }

    private JPanel wrapCard(JComponent inner) {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.add(inner, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(460, 480));
        holder.add(card);
        return holder;
    }

    // --- 1. LOGIN PANEL ---
    private JPanel buildLoginPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        
        JLabel title = new JLabel("Welcome to ATM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);
        p.add(title, g);

        g.gridwidth = 1; g.gridy++; g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL;
        p.add(UITheme.label("USER ID"), g);
        g.gridy++; userIdField = new UITheme.TextInput(15); p.add(userIdField, g);

        g.gridy++; p.add(UITheme.label("PIN"), g);
        g.gridy++; pinField = new UITheme.PasswordInput(15); p.add(pinField, g);

        g.gridy++; g.insets = new Insets(25, 8, 8, 8); g.fill = GridBagConstraints.NONE; g.anchor = GridBagConstraints.CENTER;
        UITheme.PrimaryButton loginBtn = new UITheme.PrimaryButton("Login");
        loginBtn.setPreferredSize(new Dimension(200, 45));
        p.add(loginBtn, g);

        g.gridy++; g.insets = new Insets(10, 8, 8, 8);
        JLabel hint = new JLabel("Test: user1 / 1234");
        hint.setForeground(UITheme.TEXT_SOFT);
        p.add(hint, g);

        loginBtn.addActionListener(e -> {
            Account acc = bank.authenticate(userIdField.getText().trim(), new String(pinField.getPassword()).trim());
            if (acc != null) {
                currentAccount = acc;
                updateBalanceLabel();
                userIdField.setText(""); pinField.setText("");
                cards.show(mainPanel, "menu");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return wrapCard(p);
    }

    // --- 2. MAIN MENU ---
    private JPanel buildMenuPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;

        balanceLabel = new JLabel("Current Balance: $0.00", SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        balanceLabel.setForeground(UITheme.PRIMARY);
        p.add(balanceLabel, g);
        
        g.gridy++; menuFeedback = new JLabel(" ", SwingConstants.CENTER);
        menuFeedback.setFont(new Font("Segoe UI", Font.BOLD, 14));
        menuFeedback.setForeground(UITheme.SUCCESS);
        p.add(menuFeedback, g);

        g.gridy++; g.insets = new Insets(20, 8, 8, 8); g.gridwidth = 1;
        
        UITheme.PrimaryButton wBtn = new UITheme.PrimaryButton("Withdraw");
        UITheme.PrimaryButton dBtn = new UITheme.PrimaryButton("Deposit");
        UITheme.PrimaryButton tBtn = new UITheme.PrimaryButton("Transfer");
        UITheme.PrimaryButton hBtn = new UITheme.PrimaryButton("History", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        UITheme.PrimaryButton lBtn = new UITheme.PrimaryButton("Logout", UITheme.DANGER, UITheme.DANGER_HOV, Color.WHITE);

        wBtn.setPreferredSize(new Dimension(180, 45)); dBtn.setPreferredSize(new Dimension(180, 45));
        tBtn.setPreferredSize(new Dimension(180, 45)); hBtn.setPreferredSize(new Dimension(180, 45));
        lBtn.setPreferredSize(new Dimension(180, 45));

        p.add(wBtn, g);
        g.gridx = 1; p.add(dBtn, g);
        g.gridx = 0; g.gridy++; p.add(tBtn, g);
        g.gridx = 1; p.add(hBtn, g);
        g.gridx = 0; g.gridy++; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        p.add(lBtn, g);

        wBtn.addActionListener(e -> { menuFeedback.setText(" "); cards.show(mainPanel, "withdraw"); });
        dBtn.addActionListener(e -> { menuFeedback.setText(" "); cards.show(mainPanel, "deposit"); });
        tBtn.addActionListener(e -> { menuFeedback.setText(" "); cards.show(mainPanel, "transfer"); });
        hBtn.addActionListener(e -> { updateHistory(); cards.show(mainPanel, "history"); });
        
        lBtn.addActionListener(e -> {
            currentAccount = null;
            cards.show(mainPanel, "login");
        });

        return wrapCard(p);
    }

    // --- 3. ACTION PANEL (Withdraw, Deposit, Transfer) ---
    private JPanel buildActionPanel(String title, String action) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8); g.gridx = 0; g.gridy = 0; g.gridwidth = 2;

        JLabel t = UITheme.heading(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 22));
        p.add(t, g);

        g.gridwidth = 1; g.gridy++; g.anchor = GridBagConstraints.WEST; g.fill = GridBagConstraints.HORIZONTAL;
        
        // Use local variables so each screen has its own input fields
        UITheme.TextInput amountInput = new UITheme.TextInput(15);
        UITheme.TextInput recipientInput = new UITheme.TextInput(15);

        if (action.equals("transfer")) {
            p.add(UITheme.label("RECIPIENT ID"), g);
            g.gridy++; p.add(recipientInput, g);
            g.gridy++;
        }

        p.add(UITheme.label("AMOUNT ($)"), g);
        g.gridy++; p.add(amountInput, g);

        g.gridy++; g.gridwidth = 2; g.insets = new Insets(25, 8, 8, 8); g.anchor = GridBagConstraints.CENTER; g.fill = GridBagConstraints.NONE;
        
        UITheme.PrimaryButton submitBtn = new UITheme.PrimaryButton("Confirm");
        submitBtn.setPreferredSize(new Dimension(180, 45));
        p.add(submitBtn, g);

        g.gridy++; g.insets = new Insets(5, 8, 8, 8);
        UITheme.PrimaryButton backBtn = new UITheme.PrimaryButton("Back to Menu", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        backBtn.setPreferredSize(new Dimension(180, 35));
        p.add(backBtn, g);

        submitBtn.addActionListener(e -> {
            try {
                String input = amountInput.getText().trim();
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double amt = Double.parseDouble(input);
                if (amt <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = false;
                String msg = "";
                
                if (action.equals("withdraw")) {
                    if (currentAccount.getBalance() >= amt) {
                        currentAccount.setBalance(currentAccount.getBalance() - amt);
                        currentAccount.addTransaction(new Transaction("Withdraw", amt, "ATM Cash"));
                        success = true;
                    } else { msg = "Insufficient Funds!"; }
                } else if (action.equals("deposit")) {
                    currentAccount.setBalance(currentAccount.getBalance() + amt);
                    currentAccount.addTransaction(new Transaction("Deposit", amt, "ATM Cash"));
                    success = true;
                } else if (action.equals("transfer")) {
                    String rec = recipientInput.getText().trim();
                    if (rec.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter a recipient ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (bank.transfer(currentAccount, rec, amt)) { success = true; }
                    else { msg = "Transfer failed (Check ID or Funds)"; }
                }

                if (success) {
                    updateBalanceLabel();
                    menuFeedback.setText(action.substring(0,1).toUpperCase() + action.substring(1) + " Successful!");
                    menuFeedback.setForeground(UITheme.SUCCESS);
                    amountInput.setText(""); 
                    if (action.equals("transfer")) recipientInput.setText("");
                    cards.show(mainPanel, "menu");
                } else {
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount! Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            amountInput.setText(""); 
            if (action.equals("transfer")) recipientInput.setText("");
            cards.show(mainPanel, "menu");
        });

        return wrapCard(p);
    }

    // --- 4. HISTORY PANEL ---
    private JPanel buildHistoryPanel() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setOpaque(false);
        
        JLabel t = UITheme.heading("Transaction History");
        t.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(t, BorderLayout.NORTH);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        historyArea.setBackground(UITheme.FIELD_BG);
        historyArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        p.add(scroll, BorderLayout.CENTER);

        UITheme.PrimaryButton backBtn = new UITheme.PrimaryButton("Back to Menu", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        backBtn.setPreferredSize(new Dimension(180, 40));
        
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(backBtn);
        p.add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> cards.show(mainPanel, "menu"));
        return wrapCard(p);
    }

    private void updateBalanceLabel() {
        if (currentAccount != null) {
            balanceLabel.setText(String.format("Current Balance: $%.2f", currentAccount.getBalance()));
        }
    }

    private void updateHistory() {
        StringBuilder sb = new StringBuilder();
        if (currentAccount.getTransactions().isEmpty()) {
            sb.append("No transactions yet.");
        } else {
            for (Transaction t : currentAccount.getTransactions()) {
                sb.append(t.toString()).append("\n");
            }
        }
        historyArea.setText(sb.toString());
        historyArea.setCaretPosition(0);
    }
}
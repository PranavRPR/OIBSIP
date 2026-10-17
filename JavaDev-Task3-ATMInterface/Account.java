import java.util.ArrayList;
import java.util.List;

public class Account {
    private final String userId;
    private final String pin;
    private double balance;
    private final List<Transaction> transactions;

    public Account(String userId, String pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }
    public String getUserId() { return userId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public List<Transaction> getTransactions() { return transactions; }
    public void addTransaction(Transaction t) { transactions.add(t); }
    public boolean validatePin(String inputPin) { return this.pin.equals(inputPin); }
}
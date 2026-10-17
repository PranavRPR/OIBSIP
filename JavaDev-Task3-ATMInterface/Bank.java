import java.util.HashMap;
import java.util.Map;

public class Bank {
    private final Map<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
        // Sample accounts for testing
        accounts.put("user1", new Account("user1", "1234", 1000.00));
        accounts.put("user2", new Account("user2", "5678", 500.00));
        accounts.put("user3", new Account("user3", "0000", 2500.00));
    }

    public Account authenticate(String userId, String pin) {
        Account acc = accounts.get(userId);
        if (acc != null && acc.validatePin(pin)) {
            return acc;
        }
        return null;
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }

    public boolean transfer(Account from, String toUserId, double amount) {
        Account to = accounts.get(toUserId);
        if (to == null) return false;
        if (from.getBalance() < amount) return false;

        from.setBalance(from.getBalance() - amount);
        to.setBalance(to.getBalance() + amount);

        from.addTransaction(new Transaction("Transfer Out", amount, "To " + toUserId));
        to.addTransaction(new Transaction("Transfer In", amount, "From " + from.getUserId()));
        return true;
    }
}
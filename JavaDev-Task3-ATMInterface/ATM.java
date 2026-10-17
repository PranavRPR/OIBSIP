import java.util.Scanner;

public class ATM {
    private final Bank bank;
    private final Scanner scanner;
    private Account currentAccount;

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=====================================");
        System.out.println("          WELCOME TO JAVA ATM        ");
        System.out.println("=====================================");
        
        if (!login()) {
            System.out.println("Too many failed attempts. Card blocked.");
            return;
        }
        showMenu();
    }

    private boolean login() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            Account acc = bank.authenticate(userId, pin);
            if (acc != null) {
                currentAccount = acc;
                System.out.println("\nLogin successful. Welcome, " + userId + "!");
                return true;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Attempts remaining: " + (3 - attempts));
            }
        }
        return false;
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option (1-5): ");
            
            int choice = readInt(1, 5);
            switch (choice) {
                case 1 -> showTransactionHistory();
                case 2 -> withdraw();
                case 3 -> deposit();
                case 4 -> transfer();
                case 5 -> {
                    System.out.println("Thank you for using Java ATM. Goodbye!");
                    return;
                }
            }
        }
    }

    private void showTransactionHistory() {
        System.out.println("\n--- TRANSACTION HISTORY ---");
        if (currentAccount.getTransactions().isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : currentAccount.getTransactions()) {
                System.out.println(t);
            }
        }
        System.out.printf("Current Balance: $%.2f\n", currentAccount.getBalance());
    }

    private void withdraw() {
        System.out.print("Enter amount to withdraw: $");
        double amount = readDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        if (currentAccount.getBalance() < amount) {
            System.out.println("Insufficient Funds!");
            return;
        }
        
        currentAccount.setBalance(currentAccount.getBalance() - amount);
        currentAccount.addTransaction(new Transaction("Withdraw", amount, "Cash Withdrawal"));
        System.out.printf("Successfully withdrew $%.2f. New Balance: $%.2f\n", amount, currentAccount.getBalance());
    }

    private void deposit() {
        System.out.print("Enter amount to deposit: $");
        double amount = readDouble();
        
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        
        currentAccount.setBalance(currentAccount.getBalance() + amount);
        currentAccount.addTransaction(new Transaction("Deposit", amount, "Cash Deposit"));
        System.out.printf("Successfully deposited $%.2f. New Balance: $%.2f\n", amount, currentAccount.getBalance());
    }

    private void transfer() {
        System.out.print("Enter recipient User ID: ");
        String recipientId = scanner.nextLine().trim();
        
        if (recipientId.equals(currentAccount.getUserId())) {
            System.out.println("Cannot transfer to yourself.");
            return;
        }
        if (bank.getAccount(recipientId) == null) {
            System.out.println("Recipient account not found.");
            return;
        }

        System.out.print("Enter amount to transfer: $");
        double amount = readDouble();

        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        if (currentAccount.getBalance() < amount) {
            System.out.println("Insufficient Funds!");
            return;
        }

        if (bank.transfer(currentAccount, recipientId, amount)) {
            System.out.printf("Successfully transferred $%.2f to %s. New Balance: $%.2f\n", 
                    amount, recipientId, currentAccount.getBalance());
        } else {
            System.out.println("Transfer failed.");
        }
    }

    private int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val < min || val > max) {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
    }

    private double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a valid amount: $");
            }
        }
    }
}
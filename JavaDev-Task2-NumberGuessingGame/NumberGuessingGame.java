import java.util.*;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> roundSummaries = new ArrayList<>();
        int round = 0;
        boolean playAgain = true;

        System.out.println("========================================");
        System.out.println("      WELCOME TO NUMBER GUESSING!       ");
        System.out.println("========================================");

        while (playAgain) {
            round++;
            System.out.println("\n=== Round " + round + " ===");
            System.out.println("Select difficulty:");
            System.out.println("1. Easy   (1-50,  10 attempts)");
            System.out.println("2. Medium (1-100,  7 attempts)");
            System.out.println("3. Hard   (1-200,  5 attempts)");
            System.out.print("Choice (1-3): ");

            int diff = readInt(sc, 1, 3);
            int max, maxAttempts;
            switch (diff) {
                case 1: max = 50;  maxAttempts = 10; break;
                case 2: max = 100; maxAttempts = 7;  break;
                default: max = 200; maxAttempts = 5; break;
            }

            Random rand = new Random();
            int target = rand.nextInt(max) + 1;
            int attempts = 0;
            boolean guessed = false;

            System.out.println("\nI have picked a number between 1 and " + max + ".");
            System.out.println("You have " + maxAttempts + " attempts to guess it.");

            while (attempts < maxAttempts) {
                System.out.print("Attempt " + (attempts + 1) + "/" + maxAttempts + " - Enter your guess: ");
                int guess = readInt(sc, 1, max);
                attempts++;

                if (guess == target) {
                    System.out.println(">>> Correct! You guessed it in " + attempts + " attempts.");
                    roundSummaries.add("Round " + round + " — guessed in " + attempts + " attempts");
                    guessed = true;
                    break;
                } else if (guess > target) {
                    System.out.println(">>> Too High!");
                } else {
                    System.out.println(">>> Too Low!");
                }
            }

            if (!guessed) {
                System.out.println("\n>>> You Lost! The number was " + target);
                roundSummaries.add("Round " + round + " — lost (number was " + target + ")");
            }

            System.out.print("\nPlay again? (yes/no): ");
            String ans = sc.next().trim().toLowerCase();
            playAgain = ans.equals("yes") || ans.equals("y");
        }

        System.out.println("\n========================================");
        System.out.println("             SCORE SUMMARY              ");
        System.out.println("========================================");
        if (roundSummaries.isEmpty()) {
            System.out.println("No rounds played.");
        } else {
            for (String s : roundSummaries) {
                System.out.println("- " + s);
            }
        }
        System.out.println("========================================");
        System.out.println("Thanks for playing!");
        
        sc.close();
    }

    private static int readInt(Scanner sc, int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.next().trim());
                if (val < min || val > max) {
                    System.out.print("Invalid. Please enter a number between " + min + " and " + max + ": ");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

}
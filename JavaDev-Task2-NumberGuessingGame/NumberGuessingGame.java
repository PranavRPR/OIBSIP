import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class NumberGuessingGame extends JFrame {
    private int targetNumber;
    private int maxAttempts;
    private int currentAttempts;
    private int maxRange;
    
    private JComboBox<String> difficultyBox;
    private JLabel rangeLabel;
    private JLabel feedbackLabel;
    private JLabel attemptsLabel;
    private JTextField guessField;
    private UITheme.PrimaryButton guessBtn;
    private UITheme.PrimaryButton playAgainBtn;

    public NumberGuessingGame() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        UITheme.GradientPanel root = new UITheme.GradientPanel();
        root.setLayout(new GridBagLayout());

        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 480));

        GridBagConstraints g = new GridBagConstraints();
        
        // 1. Title
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 0, 20, 0);
        JLabel title = new JLabel("Guess The Number");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);
        card.add(title, g);

        // 2. Difficulty
        g.gridy++; g.gridwidth = 1; g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(5, 0, 5, 10);
        card.add(UITheme.label("DIFFICULTY"), g);
        
        g.gridx = 1; g.anchor = GridBagConstraints.EAST;
        difficultyBox = new JComboBox<>(new String[]{"Easy (1-50, 10 tries)", "Medium (1-100, 7 tries)", "Hard (1-200, 5 tries)"});
        difficultyBox.setFont(UITheme.F_INPUT);
        difficultyBox.setPreferredSize(new Dimension(200, 35));
        card.add(difficultyBox, g);

        // 3. Range Label
        g.gridx = 0; g.gridy++; g.gridwidth = 2; g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(25, 0, 20, 0);
        rangeLabel = new JLabel("I picked a number between 1 and 50.");
        rangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rangeLabel.setForeground(UITheme.TEXT_SOFT);
        card.add(rangeLabel, g);

        // 4. Input Field (Fixed Size!)
        g.gridy++; g.insets = new Insets(5, 0, 15, 0);
        g.fill = GridBagConstraints.NONE;
        guessField = new UITheme.TextInput(10);
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("Segoe UI", Font.BOLD, 22));
        guessField.setPreferredSize(new Dimension(180, 50)); // THIS FIXES THE TINY BOX
        card.add(guessField, g);

        // 5. Guess Button
        g.gridy++; g.insets = new Insets(5, 0, 20, 0);
        guessBtn = new UITheme.PrimaryButton("Submit Guess");
        guessBtn.setPreferredSize(new Dimension(180, 45));
        card.add(guessBtn, g);

        // 6. Feedback
        g.gridy++; g.insets = new Insets(5, 0, 5, 0);
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        card.add(feedbackLabel, g);

        // 7. Attempts
        g.gridy++; g.insets = new Insets(5, 0, 20, 0);
        attemptsLabel = new JLabel("Attempts: 0 / 10");
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        attemptsLabel.setForeground(UITheme.TEXT_SOFT);
        card.add(attemptsLabel, g);

        // 8. Play Again Button (Hidden initially)
        g.gridy++; g.insets = new Insets(0, 0, 0, 0);
        playAgainBtn = new UITheme.PrimaryButton("Play Again", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        playAgainBtn.setPreferredSize(new Dimension(180, 45));
        playAgainBtn.setVisible(false);
        card.add(playAgainBtn, g);

        root.add(card);
        setContentPane(root);

        // Listeners
        difficultyBox.addActionListener(e -> startNewGame());
        guessBtn.addActionListener(e -> submitGuess());
        guessField.addActionListener(e -> submitGuess());
        playAgainBtn.addActionListener(e -> startNewGame());

        startNewGame();
    }

    private void startNewGame() {
        int diff = difficultyBox.getSelectedIndex();
        if (diff == 0) { maxRange = 50; maxAttempts = 10; }
        else if (diff == 1) { maxRange = 100; maxAttempts = 7; }
        else { maxRange = 200; maxAttempts = 5; }

        targetNumber = new Random().nextInt(maxRange) + 1;
        currentAttempts = 0;
        
        rangeLabel.setText("I picked a number between 1 and " + maxRange + ".");
        attemptsLabel.setText("Attempts: 0 / " + maxAttempts);
        feedbackLabel.setText(" ");
        guessField.setText("");
        guessField.setEditable(true);
        guessField.requestFocus();
        
        guessBtn.setVisible(true);
        difficultyBox.setEnabled(false);
        playAgainBtn.setVisible(false);
    }

    private void submitGuess() {
        try {
            String input = guessField.getText().trim();
            if (input.isEmpty()) return; // Ignore empty submits

            int guess = Integer.parseInt(input);
            if (guess < 1 || guess > maxRange) {
                feedbackLabel.setText("Enter a number between 1 and " + maxRange);
                feedbackLabel.setForeground(UITheme.DANGER);
                return;
            }

            currentAttempts++;
            attemptsLabel.setText("Attempts: " + currentAttempts + " / " + maxAttempts);

            if (guess == targetNumber) {
                winGame();
            } else if (currentAttempts >= maxAttempts) {
                loseGame();
            } else {
                if (guess > targetNumber) {
                    feedbackLabel.setText("Too High!");
                    feedbackLabel.setForeground(UITheme.DANGER);
                } else {
                    feedbackLabel.setText("Too Low!");
                    feedbackLabel.setForeground(UITheme.PRIMARY);
                }
                guessField.setText("");
            }
        } catch (NumberFormatException ex) {
            feedbackLabel.setText("Invalid Input! Enter a number.");
            feedbackLabel.setForeground(UITheme.DANGER);
        }
    }

    private void winGame() {
        feedbackLabel.setText("Correct! You won!");
        feedbackLabel.setForeground(new Color(0x10B981)); // Green
        endGame();
    }

    private void loseGame() {
        feedbackLabel.setText("You Lost! It was " + targetNumber);
        feedbackLabel.setForeground(UITheme.DANGER);
        endGame();
    }

    private void endGame() {
        guessField.setEditable(false);
        guessBtn.setVisible(false);
        playAgainBtn.setVisible(true);
        difficultyBox.setEnabled(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            new NumberGuessingGame().setVisible(true);
        });
    }
}
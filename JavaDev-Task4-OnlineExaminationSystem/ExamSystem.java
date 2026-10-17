import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExamSystem extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);
    
    private User currentUser;
    private Question[] questions;
    private int[] userAnswers;
    private int currentQuestionIndex = 0;
    private int timeRemaining = 300; // 5 mins
    private Timer timer;
    private boolean isExamActive = false;

    private JLabel timerLabel, questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionsGroup;
    private JButton nextBtn, prevBtn;

    public ExamSystem() {
        setTitle("Online Examination System");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(850, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 550));

        initQuestions();

        UITheme.GradientPanel root = new UITheme.GradientPanel();
        root.setLayout(new GridBagLayout());
        
        mainPanel.setOpaque(false);
        mainPanel.add(buildLoginPanel(), "login");
        mainPanel.add(buildProfilePanel(), "profile");
        mainPanel.add(buildExamPanel(), "exam");
        mainPanel.add(buildResultPanel(), "result");

        root.add(mainPanel);
        setContentPane(root);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
    }

    private void initQuestions() {
        questions = new Question[]{
            new Question("Which language is used for Android development?", new String[]{"Python", "Java", "C++", "Swift"}, 1),
            new Question("What does OOP stand for?", new String[]{"Object Oriented Programming", "Open Output Process", "Only One Protocol", "None"}, 0),
            new Question("Which of these is a Java keyword?", new String[]{"class", "struct", "def", "function"}, 0),
            new Question("What is the size of an int in Java?", new String[]{"2 bytes", "4 bytes", "8 bytes", "Depends on OS"}, 1),
            new Question("Which collection allows duplicate elements?", new String[]{"Set", "Map", "List", "Tree"}, 2)
        };
        userAnswers = new int[questions.length];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;
    }

    // --- 1. LOGIN PANEL ---
    private JPanel buildLoginPanel() {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        
        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 350));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 0, 30, 0);
        JLabel title = new JLabel("Student Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);
        card.add(title, g);

        g.gridwidth = 1; g.gridy++;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 0, 8, 0);
        card.add(UITheme.label("USERNAME"), g);
        g.gridx = 1; card.add(new JLabel(" "), g); // spacer
        
        g.gridx = 0; g.gridy++; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        JTextField userField = new UITheme.TextInput(18);
        card.add(userField, g);

        g.gridy++; g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(8, 0, 8, 0);
        card.add(UITheme.label("PASSWORD"), g);
        
        g.gridx = 0; g.gridy++; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passField = new UITheme.PasswordInput(18);
        card.add(passField, g);

        g.gridy++; g.insets = new Insets(30, 0, 0, 0);
        UITheme.PrimaryButton loginBtn = new UITheme.PrimaryButton("Sign In");
        card.add(loginBtn, g);

        loginBtn.addActionListener(e -> {
            String u = userField.getText().trim();
            String p = new String(passField.getPassword()).trim();
            if (u.equals("student") && p.equals("password")) {
                currentUser = new User(u, p, "Student");
                cardLayout.show(mainPanel, "profile");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.\nUse student / password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        holder.add(card);
        return holder;
    }

    // --- 2. PROFILE PANEL ---
    private JPanel buildProfilePanel() {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        
        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(400, 380));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        g.anchor = GridBagConstraints.CENTER;
        g.insets = new Insets(0, 0, 30, 0);
        JLabel title = new JLabel("Update Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);
        card.add(title, g);

        g.gridwidth = 1; g.gridy++;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(8, 0, 8, 0);
        card.add(UITheme.label("DISPLAY NAME"), g);
        
        g.gridx = 0; g.gridy++; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        JTextField nameField = new UITheme.TextInput(18);
        card.add(nameField, g);

        g.gridy++; g.gridwidth = 1; g.fill = GridBagConstraints.NONE;
        g.insets = new Insets(8, 0, 8, 0);
        card.add(UITheme.label("NEW PASSWORD"), g);
        
        g.gridx = 0; g.gridy++; g.gridwidth = 2;
        g.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passField = new UITheme.PasswordInput(18);
        card.add(passField, g);

        g.gridy++; g.insets = new Insets(30, 0, 0, 0);
        UITheme.PrimaryButton startBtn = new UITheme.PrimaryButton("Save & Start Exam");
        startBtn.setPreferredSize(new Dimension(200, 45));
        card.add(startBtn, g);

        startBtn.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPass = new String(passField.getPassword()).trim();
            if (!newName.isEmpty()) currentUser.setDisplayName(newName);
            if (!newPass.isEmpty()) currentUser.setPassword(newPass);
            startExam();
        });

        holder.add(card);
        return holder;
    }

    // --- 3. EXAM PANEL ---
    private JPanel buildExamPanel() {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        
        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setPreferredSize(new Dimension(700, 480));

        // Top: Timer
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        top.setOpaque(false);
        timerLabel = new JLabel("Time Remaining: 05:00");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(UITheme.DANGER);
        top.add(timerLabel);
        card.add(top, BorderLayout.NORTH);

        // Center: Question & Options
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        
        questionLabel = new JLabel("Question here");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        questionLabel.setForeground(UITheme.TEXT);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(questionLabel);
        center.add(Box.createVerticalStrut(20));

        optionButtons = new JRadioButton[4];
        optionsGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Segoe UI", Font.PLAIN, 15));
            optionButtons[i].setForeground(UITheme.TEXT);
            optionButtons[i].setOpaque(false);
            optionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            optionsGroup.add(optionButtons[i]);
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            center.add(optionButtons[i]);
            center.add(Box.createVerticalStrut(10));
            
            final int index = i;
            optionButtons[i].addActionListener(e -> userAnswers[currentQuestionIndex] = index);
        }
        card.add(center, BorderLayout.CENTER);

        // Bottom: Navigation
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftNav.setOpaque(false);
        prevBtn = new UITheme.PrimaryButton("Previous", UITheme.MUTED_BTN, UITheme.MUTED_BTN_H, Color.WHITE);
        prevBtn.setPreferredSize(new Dimension(110, 40));
        leftNav.add(prevBtn);
        
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightNav.setOpaque(false);
        nextBtn = new UITheme.PrimaryButton("Next");
        nextBtn.setPreferredSize(new Dimension(110, 40));
        UITheme.PrimaryButton submitBtn = new UITheme.PrimaryButton("Submit Exam", UITheme.DANGER, UITheme.DANGER_HOV, Color.WHITE);
        submitBtn.setPreferredSize(new Dimension(130, 40));
        rightNav.add(nextBtn);
        rightNav.add(submitBtn);

        bottom.add(leftNav, BorderLayout.WEST);
        bottom.add(rightNav, BorderLayout.EAST);
        card.add(bottom, BorderLayout.SOUTH);

        prevBtn.addActionListener(e -> {
            if (currentQuestionIndex > 0) { currentQuestionIndex--; loadQuestion(); }
        });

        nextBtn.addActionListener(e -> {
            if (currentQuestionIndex < questions.length - 1) { currentQuestionIndex++; loadQuestion(); }
        });

        submitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to submit?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) finishExam();
        });

        holder.add(card);
        return holder;
    }

    private void startExam() {
        currentQuestionIndex = 0;
        timeRemaining = 300;
        isExamActive = true;
        loadQuestion();
        cardLayout.show(mainPanel, "exam");
        
        if (timer != null) timer.stop();
        timer = new Timer(1000, e -> {
            timeRemaining--;
            int mins = timeRemaining / 60;
            int secs = timeRemaining % 60;
            timerLabel.setText(String.format("Time Remaining: %02d:%02d", mins, secs));
            
            if (timeRemaining <= 60) timerLabel.setForeground(UITheme.DANGER);
            
            if (timeRemaining <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time is up! Auto-submitting...");
                finishExam();
            }
        });
        timer.start();
    }

    private void loadQuestion() {
        Question q = questions[currentQuestionIndex];
        questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + q.getQuestionText());
        
        optionsGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(q.getOptions()[i]);
            if (userAnswers[currentQuestionIndex] == i) optionButtons[i].setSelected(true);
        }
        
        prevBtn.setEnabled(currentQuestionIndex > 0);
        nextBtn.setEnabled(currentQuestionIndex < questions.length - 1);
    }

    // --- 4. RESULT PANEL ---
    private JTextArea resultArea;

    private JPanel buildResultPanel() {
        JPanel holder = new JPanel(new GridBagLayout());
        holder.setOpaque(false);
        
        UITheme.CardPanel card = new UITheme.CardPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setPreferredSize(new Dimension(650, 480));
        
        JLabel title = new JLabel("Exam Results", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);
        card.add(title, BorderLayout.NORTH);
        
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(UITheme.F_MONO);
        resultArea.setBackground(UITheme.FIELD_BG);
        resultArea.setForeground(UITheme.TEXT);
        resultArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1, true));
        scroll.getViewport().setBackground(UITheme.FIELD_BG);
        card.add(scroll, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        UITheme.PrimaryButton logoutBtn = new UITheme.PrimaryButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(150, 45));
        bottom.add(logoutBtn);
        card.add(bottom, BorderLayout.SOUTH);
        
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            isExamActive = false;
            cardLayout.show(mainPanel, "login");
        });

        holder.add(card);
        return holder;
    }

    private void finishExam() {
        if (timer != null) timer.stop();
        isExamActive = false;
        
        int score = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(currentUser.getDisplayName()).append("\n");
        sb.append("--------------------------------------------------\n\n");
        
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            int ans = userAnswers[i];
            boolean correct = (ans == q.getCorrectOptionIndex());
            if (correct) score++;
            
            sb.append("Q").append(i + 1).append(": ").append(q.getQuestionText()).append("\n");
            sb.append("Your Answer: ").append(ans == -1 ? "Not Attempted" : q.getOptions()[ans]).append("\n");
            sb.append("Correct Answer: ").append(q.getOptions()[q.getCorrectOptionIndex()]).append("\n");
            sb.append("Status: ").append(correct ? "[CORRECT]" : "[INCORRECT]").append("\n\n");
        }
        
        sb.append("--------------------------------------------------\n");
        sb.append("FINAL SCORE: ").append(score).append(" / ").append(questions.length).append("\n");
        int timeTaken = 300 - timeRemaining;
        sb.append("Time Taken: ").append(timeTaken).append(" seconds\n");
        
        resultArea.setText(sb.toString());
        resultArea.setCaretPosition(0);
        cardLayout.show(mainPanel, "result");
    }

    private void handleWindowClose() {
        if (isExamActive) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to quit? Your exam will be submitted.", 
                "Confirm Quit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                finishExam();
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}
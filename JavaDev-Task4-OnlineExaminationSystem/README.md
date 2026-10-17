# Online Examination System (Premium UI)

A modern, GUI-based examination system built with Java Swing. Students log in, update their profile, answer multiple-choice questions within a timed session, and receive a detailed score breakdown. Features a custom-painted UI with dark gradients and rounded cards.

## Task Info
- Track: Java Development
- Task: Task 4 — Online Examination System
- Organisation: Oasis Infobyte (OIBSIP)

## Tech Stack
- Java 17
- Java Swing (custom UI components in `UITheme.java`)
- `javax.swing.Timer` for the countdown
- `CardLayout` for screen navigation

## Features
- **Premium UI**: Dark gradient background, white rounded cards, custom input fields with focus rings, hover states on buttons.
- **Login Screen**: Username and password authentication.
- **Profile Update**: Change display name and password before the exam.
- **Exam Screen**: 
  - One MCQ at a time with 4 RadioButton options.
  - Next and Previous buttons for navigation.
  - Countdown timer turns red under 60 seconds.
  - Auto-submits when the timer hits zero.
- **Manual Submit**: Submit button with a confirmation dialog.
- **Result Screen**: Shows final score, time taken, and a breakdown of correct/incorrect answers.
- **Session Management**: Window close button during an exam triggers a warning dialog.
- **Logout**: Returns to the login screen from the results page.

## How to Run
Compile all files and run `Main`:
```bash
javac *.java
java Main

## Test Credentials
Username	Password
student	    password
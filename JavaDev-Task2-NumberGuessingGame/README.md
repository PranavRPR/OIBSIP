# Number Guessing Game

A console-based Java game where the computer generates a random number and the player tries to guess it within a limited number of attempts. Includes difficulty levels, attempt tracking, and a multi-round score summary.

---

## 📌 Task Info

| Field | Value |
|-------|-------|
| **Track** | Java Development |
| **Task** | Task 2 — Number Guessing Game |
| **Intern** | [Your Full Name] |
| **Organisation** | Oasis Infobyte (OIBSIP) |

---

## 🛠️ Tech Stack

- **Java 17** (Core Java only — no external libraries required)
- **`java.util.Random`** — for number generation
- **`java.util.Scanner`** — for user input
- **`java.util.ArrayList`** — for score tracking across rounds

---

## ✨ Features

### Core Gameplay
- System generates a random number at the start of each round.
- Player is prompted to enter a guess.
- After each guess, the game displays: **"Too High!"**, **"Too Low!"**, or **"Correct!"**.
- Attempt counter visible at all times (e.g., `Attempt 3/7`).
- Game ends with a **"You Lost!"** message if the maximum attempts are reached, revealing the correct number.
- **"Play Again"** option after each round (`yes/no` prompt).

### Score Tracking
- Tracks results across multiple rounds.
- Displays a summary at the end: `Round X — guessed in Y attempts` or `Round X — lost (number was Z)`.

### 🎁 Bonus: Difficulty Levels
- **Easy:** 1–50, 10 attempts
- **Medium:** 1–100, 7 attempts
- **Hard:** 1–200, 5 attempts

---

## 🚀 How to Run

Since this is a pure console application using only the standard Java library, you do not need Maven. 

### Prerequisites
- Java 17 or later installed (`java -version`).

### Compile & Run
```bash
# 1. Compile the code
javac NumberGuessingGame.java

# 2. Run the game
java NumberGuessingGame
# OIBSIP


Welcome to my **OIBSIP** repository! 

This repository contains all 5 completed tasks for the Java Development track. The projects showcase a progression from core Object-Oriented Programming to desktop GUI applications (Java Swing) with database integration, culminating in a full-stack Spring Boot web application.

---

## 🛠️ Technologies Used Across Projects
- **Languages:** Java 17, SQL, HTML/CSS
- **Frameworks/Libraries:** Java Swing (Desktop GUI), Spring Boot (Web)
- **Databases:** SQLite, JDBC
- **Build Tools:** Maven

---

## 📋 Task Overview

| # | Task Name | Tech Stack | Link to Project |
|---|-----------|------------|-----------------|
| 1 | **Online Reservation System** | Swing, JDBC, SQLite | [View Code](./JavaDev-Task1-OnlineReservationSystem) |
| 2 | **Number Guessing Game** | Swing GUI | [View Code](./JavaDev-Task2-NumberGuessingGame) |
| 3 | **ATM Interface** | Swing GUI, Core OOP | [View Code](./JavaDev-Task3-ATMInterface) |
| 4 | **Online Examination System** | Swing GUI, Timers | [View Code](./JavaDev-Task4-OnlineExaminationSystem) |
| 5 | **Digital Library Management System** | Spring Boot, SQLite, HTML/CSS | [View Code](./JavaDev-Task5-DigitalLibrary) |

---

## 🔍 Detailed Task Breakdown

### 📌 Task 1: Online Reservation System
A premium GUI application to book and cancel train tickets.
- **Features:** Login authentication, auto-generated PNR, auto-filled train names, confirmation dialogs, and cancellation with validation.
- **UI:** Custom dark-mode gradient with floating white cards.
- **Test Credentials:** `admin` / `admin123`
- **[Read more...](./JavaDev-Task1-OnlineReservationSystem/README.md)**

### 📌 Task 2: Number Guessing Game
A desktop GUI game where the player guesses a random number.
- **Features:** Difficulty levels (Easy/Medium/Hard), live "Too High/Too Low" feedback, attempt limits, and visual win/loss states.
- **UI:** Centered card layout with custom rounded input fields.
- **[Read more...](./JavaDev-Task2-NumberGuessingGame/README.md)**

### 📌 Task 3: ATM Interface
A banking simulation built with a 5-class OOP architecture.
- **Features:** 3-attempt PIN limit, deposit/withdraw/transfer, real-time balance updates, and a timestamped transaction history.
- **UI:** Fully interactive GUI replacing the console menu.
- **Test Credentials:** `user1` / `1234`
- **[Read more...](./JavaDev-Task3-ATMInterface/README.md)**

### 📌 Task 4: Online Examination System
A timed examination platform.
- **Features:** Student login, profile updates, one-question-at-a-time navigation, live countdown timer, auto-submit on timeout, and a detailed result breakdown.
- **UI:** Premium Swing components with `CardLayout` for seamless screen transitions.
- **Test Credentials:** `student` / `password`
- **[Read more...](./JavaDev-Task4-OnlineExaminationSystem/README.md)**

### 📌 Task 5: Digital Library Management System
A full-stack web application.
- **Features:** Admin and User roles, book inventory management, issuing/returning books, and automatic fine calculation ($5/day for late returns).
- **Tech Stack:** Spring Boot, SQLite, Thymeleaf, Custom CSS.
- **Test Credentials:** Admin (`admin` / `admin123`) | User (`student` / `student123`)
- **[Read more...](./JavaDev-Task5-DigitalLibrary/README.md)**

---

## ⚙️ How to Run the Projects

### 1. Core Java & Swing Apps (Tasks 1, 2, 3, 4)
Navigate to the specific task folder, compile, and run:
```bash
cd JavaDev-Task1-OnlineReservationSystem
mvn clean compile exec:java
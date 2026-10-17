# Digital Library Management System

A web-based library system built with Spring Boot, SQLite, Thymeleaf, and Tailwind CSS. It features separate Admin and User roles, book issuing/returning with automatic fine calculation, and a premium responsive UI.

## Task Info
- Track: Java Development
- Task: Task 5 — Digital Library Management System
- Organisation: Oasis Infobyte (OIBSIP)

## Tech Stack
- Java 17
- Spring Boot 3 (Web, Thymeleaf, JDBC)
- SQLite (embedded database)
- Tailwind CSS (via CDN for styling)
- Maven

## Features
### Admin Module
- Secure Admin Login
- View inventory and all issued books
- Add new books (Title, Author, Category, Quantity)

### User Module
- User Login
- Browse the book catalogue
- Issue a book (decrements available quantity)
- Return a book (increments available quantity)
- Automatic fine calculation: $5 per day if returned after the 7-day due date

## How to Run
```bash
mvn spring-boot:run

## The application will start at http://localhost:8080.

## Test Credentials
Role	Username	Password
Admin	admin	    admin123
User	student	    student123
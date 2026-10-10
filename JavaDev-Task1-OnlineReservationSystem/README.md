# Online Reservation System

A premium GUI-based train reservation system built with Java Swing, JDBC and SQLite. Users can log in, book tickets with auto-generated PNR numbers, and cancel bookings by PNR.

## Task Info
- Track: Java Development
- Task: Task 1 — Online Reservation System
- Organisation: Oasis Infobyte (OIBSIP)

## Tech Stack
- Java 17
- Swing (custom-themed GUI)
- JDBC (PreparedStatement for SQL injection prevention)
- SQLite (embedded, no server required)
- Maven

## Features
### Login
- Username + password authentication
- Empty-field validation
- Access-denied dialog for invalid credentials

### Book Ticket
- Passenger name, train number, auto-populated train name, class type, journey date, source, destination
- Auto-filled train name when train number is selected
- Date defaults to today; validated as yyyy-MM-dd
- Unique PNR generated on success
- Confirmation dialog with full booking details

### Cancel Ticket
- Enter PNR → Fetch → booking details displayed
- Confirm cancellation with "Are you sure?" dialog
- Booking permanently removed from database

## How to Run
mvn clean compile exec:java


## Login Credentials
| Username | Password |
|----------|----------|
| admin    | admin123 |

## Sample Trains
| Number | Name |
|--------|------|
| 12345 | Rajdhani Express |
| 12346 | Shatabdi Express |
| 12347 | Duronto Express |
| 12348 | Garib Rath Express |
| 12349 | Tejas Express |

## Project Structure
JavaDev-Task1-OnlineReservationSystem/
├── pom.xml
├── README.md
├── .gitignore
├── screenshots/
└── src/main/java/com/oibsip/reservation/
├── Main.java
├── Database.java
├── TrainData.java
├── UITheme.java
├── LoginFrame.java
└── ReservationFrame.java


## Feature Checklist (OIBSIP)
- [x] Login form with access denied for invalid credentials
- [x] Reservation form with auto-populated train name
- [x] Book button saves to DB and generates unique PNR
- [x] Confirmation dialog with booking details
- [x] Cancellation form: PNR input + Fetch button
- [x] Confirm cancellation dialog before deletion
- [x] Input validation: no empty fields, numeric train number, valid date
- [x] PreparedStatement used (SQL injection prevention)

## Author
[Pranav Ramraje] — Java Development Intern, Oasis Infobyte
GitHub: @PranavRPR

## Acknowledgements
- Oasis Infobyte for the internship opportunity
- Xerial SQLite JDBC for the embedded database driver

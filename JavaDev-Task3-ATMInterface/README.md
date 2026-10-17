
# ATM Interface

A console-based simulation of an ATM machine built with Core Java, demonstrating Object-Oriented Programming (OOP) principles. Users can authenticate with a User ID and PIN to perform standard banking transactions.


## Task Info
- Track: Java Development
- Task: Task 3 — ATM Interface
- Organisation: Oasis Infobyte (OIBSIP)

## Tech Stack
- Java 17
- OOP (Encapsulation, Classes, Objects)
- `ArrayList` (Transaction History)
- `Scanner` (Console Input)

## Features
- **Authentication**: User ID and PIN login with a maximum of 3 attempts.
- **Transaction History**: View a log of all past transactions in the current session, formatted with timestamps.
- **Withdraw**: Validate sufficient funds before deducting the balance and logging the transaction.
- **Deposit**: Add funds to the balance and log the transaction.
- **Transfer**: Transfer funds to another account, with balance validation and logging on both ends.
- **Quit**: Safely exit the application.

## Architecture (5 Distinct Classes)
1. **Main**: Entry point of the application.
2. **ATM**: Handles the console UI, login loop, menu, and user interactions.
3. **Bank**: Manages the collection of accounts and handles the transfer logic between accounts.
4. **Account**: Represents a user account with encapsulated balance, PIN, and an `ArrayList` for transaction history.
5. **Transaction**: Represents a single transaction with type, amount, timestamp, and description.

## How to Run
Compile all files and run `Main`:
```bash
javac *.java
java Main

## Test Credentials
UserID	PIN	    Initial Balance
user1	1234	$1000.00
user2	5678	$500.00
user3	0000	$2500.00

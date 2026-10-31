# Project Report: Stock Trading Platform

## 1. Introduction
The Stock Trading Platform is a menu-driven Java application that simulates basic stock transactions. A user can view a predefined stock market, buy and sell shares using demo funds, inspect holdings, and review transaction history.

## 2. Problem Statement
Beginners often need a safe way to understand the basic workflow of stock transactions without connecting to a real brokerage account. This project models the core operations using a small console application.

## 3. Objectives
- Apply object-oriented programming in a practical project.
- Represent stocks, users, and transactions as Java objects.
- Validate purchases and sales to prevent invalid operations.
- Use collections to manage market data and portfolio holdings.
- Persist basic account and transaction information in files.

## 4. Scope
This is a local educational simulation with hard-coded demo prices and a single demo user. It does not provide investment advice, real-time prices, or actual trading.

## 5. System Requirements
**Software:** Java JDK 8+, Jvdroid or another Java IDE, Git/GitHub (for version control).

**Hardware:** Android phone capable of running Jvdroid, or a computer with a Java runtime.

## 6. Design
- **Main:** menu, input handling, market setup, transaction workflow, and persistence.
- **Stock:** immutable stock symbol, company name, and demo price.
- **User:** cash balance and portfolio; applies buy/sell rules.
- **Transaction:** records trade type, symbol, quantity, price, total, and timestamp.

## 7. Workflow
1. Initialize the demo market.
2. Load the saved account if present; otherwise create a demo account with ₹100,000.
3. Display the menu.
4. Perform the selected operation after validating input.
5. Update balance and holdings for valid trades.
6. Save account data and append the transaction log.
7. Save data on exit.

## 8. Validation Rules
- Stock symbols must exist in the demo market.
- Quantity must be a positive whole number.
- A purchase cannot exceed available cash.
- A sale cannot exceed shares held.
- Added demo funds must be positive.
- Invalid numeric input is requested again.

## 9. Concepts Used
- Classes and objects
- Encapsulation and private fields
- Constructors and validation
- `Map` / `LinkedHashMap`
- `Scanner` input handling
- Exception handling
- File I/O
- Date and time formatting
- Method decomposition

## 10. Testing
Refer to `TEST_CASES.md` for manual functional tests covering valid operations and error cases.

## 11. Limitations
Prices do not change and are not fetched from a live exchange. Data is stored as plain text, there is no authentication, and the program is not designed for real financial transactions.

## 12. Future Enhancements
- Store users and transactions in MySQL using JDBC.
- Add login and role-based access.
- Integrate a legitimate market-data API.
- Add transaction fees and profit/loss calculations.
- Add automated unit tests.
- Build a Spring Boot REST API and a web/mobile user interface.

## 13. Conclusion
The project demonstrates a practical application of Core Java, collections, validation, and persistence through a simple trading simulation. It provides a foundation for future backend development.

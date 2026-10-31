# Stock Trading Platform (Java)

A console-based stock trading simulation built with Core Java. It demonstrates object-oriented programming, collections, input validation, transaction logging, and file-based persistence.

> **Educational project only:** prices are fixed demo values. This is not connected to a stock exchange or brokerage and does not execute real trades or handle real money.

## Features

- View a demo market of six listed companies
- Buy shares after checking quantity and available cash
- Sell only shares already held in the portfolio
- View portfolio quantities, holdings value, cash balance, and total account value
- View transaction history with timestamps
- Add demo funds
- Save and restore account balance and holdings using text files
- Validate invalid menu input, symbols, quantities, and insufficient funds

## Technology

- Java (standard library; no external dependencies)
- `Map` / `LinkedHashMap` for market and holdings
- Encapsulation and classes (`Stock`, `User`, `Transaction`)
- Exception handling and input validation
- File I/O with buffered readers/writers
- `LocalDateTime` for transaction timestamps

## Run on Android using Jvdroid

1. Download or clone this repository.
2. Open `Main.java` in Jvdroid.
3. Run the program.
4. Use the menu in the console.
5. Jvdroid should create a `data` folder in its working directory when the program first saves. If your app uses a temporary working directory, keep the project in a persistent folder and run it from there.

## Run on a computer

Install JDK 8 or newer, open a terminal in the project folder, and run:

```bash
javac Main.java
java Main
```

## Demo market

| Symbol | Company | Demo price |
|---|---|---:|
| TCS | Tata Consultancy Services | ₹3,850 |
| INFY | Infosys | ₹1,780 |
| RELIANCE | Reliance Industries | ₹2,940 |
| HDFCBANK | HDFC Bank | ₹1,680 |
| WIPRO | Wipro | ₹520 |
| ITC | ITC Limited | ₹460 |

These prices are hard-coded for demonstration and do not update automatically.

## Data files

The application stores:
- `data/account.txt` — user name, cash balance, and holdings
- `data/transactions.txt` — append-only transaction log

Do not edit these files while the application is running. The files are local demo data, not secure financial storage.

## Basic test checklist

1. View the market; all six symbols should appear.
2. Buy 2 WIPRO shares; the balance should decrease by ₹1,040.
3. Try buying an unknown symbol; the program should reject it.
4. Try buying more shares than the balance permits; the program should reject it.
5. Sell 1 WIPRO share; the balance should increase by ₹520.
6. Try selling more shares than owned; the program should reject it.
7. View transaction history; BUY and SELL records should appear.
8. Exit and restart; saved cash balance and holdings should be restored.

## Limitations and future improvements

- Fixed demo prices; no live market API
- One local demo user; no login/authentication
- Plain-text local files; no database or encryption
- No brokerage fees, taxes, or price fluctuations
- Future work: JDBC + MySQL, user authentication, live market data from a permitted provider, REST API with Spring Boot, unit tests, and a GUI/web client.

## Project structure

```text
StockTradingPlatform/
├── Main.java
├── README.md
├── PROJECT_REPORT.md
├── TEST_CASES.md
├── .gitignore
└── data/
    └── .gitkeep
```

## Author

Ashish Singh

Built as a learning project for a Java Programming internship. Review the code and understand each feature before presenting or submitting it.

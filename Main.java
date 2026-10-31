import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Stock Trading Platform - console-based Java project.
 * Educational simulation only: no real brokerage or live market connection.
 */
public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String DATA_DIR = "data";
    private static final String ACCOUNT_FILE = DATA_DIR + "/account.txt";
    private static final String TRANSACTION_FILE = DATA_DIR + "/transactions.txt";
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Map<String, Stock> market = new LinkedHashMap<>();
    private static User user;

    public static void main(String[] args) {
        initializeMarket();
        user = loadAccount();
        if (user == null) {
            user = new User("Ashish", 100000.00);
            saveAccount();
        }

        System.out.println("==============================================");
        System.out.println("          STOCK TRADING PLATFORM");
        System.out.println("==============================================");
        System.out.println("Educational simulation | No real money involved");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: showMarket(); break;
                case 2: showBalance(); break;
                case 3: buyStock(); break;
                case 4: sellStock(); break;
                case 5: showPortfolio(); break;
                case 6: showTransactionHistory(); break;
                case 7: addFunds(); break;
                case 8: saveAccount(); System.out.println("Data saved."); break;
                case 0:
                    saveAccount();
                    System.out.println("Your data has been saved. Goodbye!");
                    running = false;
                    break;
                default: System.out.println("Invalid option. Please choose from the menu.");
            }
        }
        SCANNER.close();
    }

    private static void initializeMarket() {
        market.put("TCS", new Stock("TCS", "Tata Consultancy Services", 3850.00));
        market.put("INFY", new Stock("INFY", "Infosys", 1780.00));
        market.put("RELIANCE", new Stock("RELIANCE", "Reliance Industries", 2940.00));
        market.put("HDFCBANK", new Stock("HDFCBANK", "HDFC Bank", 1680.00));
        market.put("WIPRO", new Stock("WIPRO", "Wipro", 520.00));
        market.put("ITC", new Stock("ITC", "ITC Limited", 460.00));
    }

    private static void printMenu() {
        System.out.println("\n--------------- MAIN MENU ----------------");
        System.out.println("1. View market");
        System.out.println("2. View cash balance");
        System.out.println("3. Buy stocks");
        System.out.println("4. Sell stocks");
        System.out.println("5. View portfolio");
        System.out.println("6. Transaction history");
        System.out.println("7. Add demo funds");
        System.out.println("8. Save data");
        System.out.println("0. Exit");
        System.out.println("------------------------------------------");
    }

    private static void showMarket() {
        System.out.println("\n---------------- MARKET ----------------");
        System.out.printf("%-12s %-32s %12s%n", "SYMBOL", "COMPANY", "PRICE (INR)");
        for (Stock stock : market.values()) {
            System.out.printf("%-12s %-32s ₹%11.2f%n",
                    stock.getSymbol(), stock.getCompanyName(), stock.getPrice());
        }
        System.out.println("Note: These are fixed demo prices, not live market prices.");
    }

    private static void showBalance() {
        System.out.printf("%nAccount holder: %s%nAvailable cash: ₹%.2f%n",
                user.getName(), user.getCashBalance());
    }

    private static void buyStock() {
        showMarket();
        String symbol = readSymbol("Enter stock symbol to buy: ");
        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Stock not found. Please use a symbol shown in the market.");
            return;
        }

        int quantity = readInt("Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        double totalCost = stock.getPrice() * quantity;
        if (totalCost > user.getCashBalance()) {
            System.out.printf("Insufficient funds. Required: ₹%.2f, available: ₹%.2f%n",
                    totalCost, user.getCashBalance());
            return;
        }

        user.buy(stock, quantity);
        recordTransaction(new Transaction("BUY", symbol, quantity, stock.getPrice(), totalCost));
        saveAccount();
        System.out.printf("Purchase successful: %d share(s) of %s for ₹%.2f.%n",
                quantity, symbol, totalCost);
    }

    private static void sellStock() {
        if (user.getPortfolio().isEmpty()) {
            System.out.println("Your portfolio is empty. Buy stocks before selling.");
            return;
        }
        showPortfolio();
        String symbol = readSymbol("Enter stock symbol to sell: ");
        Stock stock = market.get(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        int owned = user.getPortfolio().getOrDefault(symbol, 0);
        if (owned == 0) {
            System.out.println("You do not own any shares of " + symbol + ".");
            return;
        }

        int quantity = readInt("Enter quantity to sell (owned: " + owned + "): ");
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }
        if (quantity > owned) {
            System.out.println("You cannot sell more shares than you own.");
            return;
        }

        double proceeds = stock.getPrice() * quantity;
        user.sell(symbol, quantity, proceeds);
        recordTransaction(new Transaction("SELL", symbol, quantity, stock.getPrice(), proceeds));
        saveAccount();
        System.out.printf("Sale successful: %d share(s) of %s for ₹%.2f.%n",
                quantity, symbol, proceeds);
    }

    private static void showPortfolio() {
        Map<String, Integer> portfolio = user.getPortfolio();
        System.out.println("\n---------------- PORTFOLIO ----------------");
        if (portfolio.isEmpty()) {
            System.out.println("No holdings yet.");
            System.out.println("-------------------------------------------");
            return;
        }
        System.out.printf("%-12s %-30s %8s %14s %14s%n",
                "SYMBOL", "COMPANY", "QUANTITY", "PRICE", "MARKET VALUE");
        double totalValue = 0.0;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            Stock stock = market.get(entry.getKey());
            int quantity = entry.getValue();
            double value = stock.getPrice() * quantity;
            totalValue += value;
            System.out.printf("%-12s %-30s %8d ₹%13.2f ₹%13.2f%n",
                    stock.getSymbol(), stock.getCompanyName(), quantity,
                    stock.getPrice(), value);
        }
        System.out.printf("Total holdings value: ₹%.2f%n", totalValue);
        System.out.printf("Cash balance:        ₹%.2f%n", user.getCashBalance());
        System.out.printf("Total account value:  ₹%.2f%n", totalValue + user.getCashBalance());
        System.out.println("-------------------------------------------");
    }

    private static void showTransactionHistory() {
        File file = new File(TRANSACTION_FILE);
        System.out.println("\n------------- TRANSACTION HISTORY -------------");
        if (!file.exists() || file.length() == 0) {
            System.out.println("No transactions recorded yet.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.printf("%-20s %-6s %-12s %8s %12s %12s%n",
                    "DATE/TIME", "TYPE", "SYMBOL", "QUANTITY", "PRICE", "TOTAL");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length == 6) {
                    System.out.printf("%-20s %-6s %-12s %8s ₹%11s ₹%11s%n",
                            parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read transaction history: " + e.getMessage());
        }
        System.out.println("-----------------------------------------------");
    }

    private static void addFunds() {
        double amount = readDouble("Enter demo amount to add (₹): ");
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }
        user.addFunds(amount);
        saveAccount();
        System.out.printf("₹%.2f added. New balance: ₹%.2f%n",
                amount, user.getCashBalance());
    }

    private static void recordTransaction(Transaction transaction) {
        File directory = new File(DATA_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Warning: could not create data directory.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE, true))) {
            writer.write(transaction.toFileLine());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Warning: transaction history could not be saved: " + e.getMessage());
        }
    }

    private static User loadAccount() {
        File file = new File(ACCOUNT_FILE);
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String name = reader.readLine();
            String balanceLine = reader.readLine();
            if (name == null || balanceLine == null) return null;
            double balance = Double.parseDouble(balanceLine);
            User loaded = new User(name, balance);
            String holdingLine;
            while ((holdingLine = reader.readLine()) != null) {
                String[] parts = holdingLine.split(",", -1);
                if (parts.length == 2 && market.containsKey(parts[0])) {
                    int quantity = Integer.parseInt(parts[1]);
                    if (quantity > 0) loaded.restoreHolding(parts[0], quantity);
                }
            }
            return loaded;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Saved account data could not be loaded; starting a new demo account.");
            return null;
        }
    }

    private static void saveAccount() {
        File directory = new File(DATA_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            System.out.println("Warning: could not create data directory.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
            writer.write(user.getName());
            writer.newLine();
            writer.write(String.valueOf(user.getCashBalance()));
            writer.newLine();
            for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Warning: account data could not be saved: " + e.getMessage());
        }
    }

    private static String readSymbol(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim().toUpperCase(Locale.ROOT);
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}

class Stock {
    private final String symbol;
    private final String companyName;
    private final double price;

    public Stock(String symbol, String companyName, double price) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Stock symbol cannot be empty.");
        }
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be empty.");
        }
        if (price <= 0) throw new IllegalArgumentException("Price must be positive.");
        this.symbol = symbol.toUpperCase(Locale.ROOT);
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return symbol + " - " + companyName + " (₹" + String.format("%.2f", price) + ")";
    }
}

class User {
    private final String name;
    private double cashBalance;
    private final Map<String, Integer> portfolio = new LinkedHashMap<>();

    public User(String name, double initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (initialBalance < 0) throw new IllegalArgumentException("Balance cannot be negative.");
        this.name = name;
        this.cashBalance = initialBalance;
    }

    public String getName() { return name; }
    public double getCashBalance() { return cashBalance; }

    // Return a copy so callers cannot directly replace the portfolio map.
    public Map<String, Integer> getPortfolio() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(portfolio));
    }

    public void buy(Stock stock, int quantity) {
        if (stock == null) throw new IllegalArgumentException("Stock cannot be null.");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        double cost = stock.getPrice() * quantity;
        if (cost > cashBalance) throw new IllegalStateException("Insufficient balance.");
        cashBalance -= cost;
        portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + quantity);
    }

    public void sell(String symbol, int quantity, double proceeds) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive.");
        int owned = portfolio.getOrDefault(symbol, 0);
        if (quantity > owned) throw new IllegalStateException("Not enough shares.");
        int remaining = owned - quantity;
        if (remaining == 0) portfolio.remove(symbol);
        else portfolio.put(symbol, remaining);
        cashBalance += proceeds;
    }

    public void restoreHolding(String symbol, int quantity) {
        if (symbol == null || quantity <= 0) throw new IllegalArgumentException("Invalid saved holding.");
        portfolio.put(symbol, quantity);
    }

    public void addFunds(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        cashBalance += amount;
    }
}

class Transaction {
    private final String type;
    private final String symbol;
    private final int quantity;
    private final double price;
    private final double total;
    private final String timestamp;

    public Transaction(String type, String symbol, int quantity, double price, double total) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String toFileLine() {
        return timestamp + "|" + type + "|" + symbol + "|" + quantity + "|"
                + String.format(Locale.US, "%.2f", price) + "|"
                + String.format(Locale.US, "%.2f", total);
    }
}

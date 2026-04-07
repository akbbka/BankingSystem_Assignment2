import java.util.*;

class BankAccount {
    String accountNumber;
    String username;
    double balance;

    public BankAccount(String accountNumber, String username, double balance) {
        this.accountNumber = accountNumber;
        this.username = username;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return username + " (Acc: " + accountNumber + ") - Balance: " + balance;
    }
}

public class Main {
    static LinkedList<BankAccount> accounts = new LinkedList<>();
    static Stack<String> transactionHistory = new Stack<>();
    static Queue<String> billQueue = new LinkedList<>();
    static Queue<BankAccount> accountRequests = new LinkedList<>();

    static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Physical Structure (Array)
        System.out.println("--- Task 6: Physical Data Structure (Array) ---");
        BankAccount[] fixedAccounts = new BankAccount[3];
        fixedAccounts[0] = new BankAccount("101", "Ali", 150000);
        fixedAccounts[1] = new BankAccount("102", "Sara", 220000);
        fixedAccounts[2] = new BankAccount("103", "Kris", 50000);
        for (BankAccount acc : fixedAccounts) System.out.println(acc);
        System.out.println("-----------------------------------------------\n");

        while (true) {
            System.out.println("\nMAIN MENU: 1-Enter Bank, 2-Enter ATM, 3-Admin Area, 4-Exit");
            System.out.print("Select: ");
            if (!sc.hasNextInt()) { sc.next(); continue; }
            int choice = sc.nextInt();
            if (choice == 4) break;

            switch (choice) {
                case 1: // Bank Menu
                    System.out.println("--- Bank Menu ---");
                    System.out.println("1-Request Account Opening");
                    System.out.println("2-Deposit");
                    System.out.println("3-Withdraw");
                    System.out.println("4-Undo Last Transaction");
                    System.out.println("5-View Last Transaction (Peek)");
                    System.out.println("6-Add Bill Payment");
                    System.out.println("7-Search Account by Username");
                    System.out.print("Select: ");
                    if (!sc.hasNextInt()) { sc.next(); break; }
                    int bChoice = sc.nextInt();

                    if (bChoice == 1) {
                        // Account opening request
                        System.out.print("Enter your name: ");
                        String name = sc.next();
                        BankAccount req = new BankAccount("ACC" + (accounts.size() + accountRequests.size() + 1), name, 0);
                        accountRequests.add(req);
                        System.out.println("Request added to queue. Wait for admin to process.");

                    } else if (bChoice == 2 || bChoice == 3) {
                        // Deposit & Withdraw
                        handleTransaction(sc, bChoice);

                    } else if (bChoice == 4) {
                        // Undo (pop)
                        if (!transactionHistory.isEmpty()) {
                            System.out.println("Undo: " + transactionHistory.pop() + " → removed");
                        } else {
                            System.out.println("No transactions to undo.");
                        }

                    } else if (bChoice == 5) {
                        // Peek (display last without removing)
                        if (!transactionHistory.isEmpty()) {
                            System.out.println("Last transaction: " + transactionHistory.peek());
                        } else {
                            System.out.println("No transactions yet.");
                        }

                    } else if (bChoice == 6) {
                        // Add bill payment to queue
                        System.out.print("Enter bill name (e.g. Electricity, Internet): ");
                        String bill = sc.next();
                        billQueue.add(bill + " Bill");
                        transactionHistory.push("Bill payment: " + bill + " Bill");
                        System.out.println("Added: " + bill + " Bill");

                    } else if (bChoice == 7) {
                        // Search by username
                        System.out.print("Enter username to search: ");
                        String searchName = sc.next();
                        boolean found = false;
                        for (BankAccount acc : accounts) {
                            if (acc.username.equalsIgnoreCase(searchName)) {
                                System.out.println("Found: " + acc);
                                found = true;
                                break;
                            }
                        }
                        if (!found) System.out.println("Account not found.");

                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;

                case 2: // ATM Menu
                    System.out.println("--- ATM Menu ---");
                    System.out.println("1-Balance Enquiry");
                    System.out.println("2-Withdraw");
                    System.out.print("Select: ");
                    if (!sc.hasNextInt()) { sc.next(); break; }
                    int atmChoice = sc.nextInt();

                    if (atmChoice == 1) {
                        // Balance enquiry
                        System.out.print("Enter username: ");
                        String user = sc.next();
                        boolean found = false;
                        for (BankAccount acc : accounts) {
                            if (acc.username.equalsIgnoreCase(user)) {
                                System.out.println("Balance: " + acc.balance);
                                found = true;
                                break;
                            }
                        }
                        if (!found) System.out.println("Account not found.");

                    } else if (atmChoice == 2) {
                        // ATM Withdraw
                        handleTransaction(sc, 3);

                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;

                case 3: // Admin Menu
                    System.out.println("--- Admin Menu ---");
                    System.out.println("1-Process Next Account Request");
                    System.out.println("2-Process Next Bill Payment");
                    System.out.println("3-View All Accounts");
                    System.out.println("4-View Pending Account Requests");
                    System.out.println("5-View Pending Bill Queue");
                    System.out.print("Select: ");
                    if (!sc.hasNextInt()) { sc.next(); break; }
                    int aChoice = sc.nextInt();

                    if (aChoice == 1) {
                        // Process account request from queue
                        if (!accountRequests.isEmpty()) {
                            BankAccount newAcc = accountRequests.poll();
                            accounts.add(newAcc);
                            System.out.println("Account opened for: " + newAcc.username + " | " + newAcc.accountNumber);
                        } else {
                            System.out.println("No pending account requests.");
                        }

                    } else if (aChoice == 2) {
                        // Process bill from queue
                        if (!billQueue.isEmpty()) {
                            System.out.println("Processing: " + billQueue.poll());
                            if (!billQueue.isEmpty()) {
                                System.out.println("Remaining: " + billQueue.peek());
                            } else {
                                System.out.println("No more bills in queue.");
                            }
                        } else {
                            System.out.println("No bills in queue.");
                        }

                    } else if (aChoice == 3) {
                        // Display all accounts
                        if (accounts.isEmpty()) {
                            System.out.println("No accounts found.");
                        } else {
                            System.out.println("Accounts List:");
                            int i = 1;
                            for (BankAccount acc : accounts) {
                                System.out.println(i++ + ". " + acc);
                            }
                        }

                    } else if (aChoice == 4) {
                        // View pending requests
                        if (accountRequests.isEmpty()) {
                            System.out.println("No pending account requests.");
                        } else {
                            System.out.println("Pending Account Requests:");
                            for (BankAccount req : accountRequests) {
                                System.out.println("  - " + req.username);
                            }
                        }

                    } else if (aChoice == 5) {
                        // View bill queue
                        if (billQueue.isEmpty()) {
                            System.out.println("Bill queue is empty.");
                        } else {
                            System.out.println("Pending Bills:");
                            for (String bill : billQueue) {
                                System.out.println("  - " + bill);
                            }
                        }

                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1-4.");
            }
        }

        System.out.println("Thank you for using our banking system. Goodbye!");
        sc.close();
    }

    private static void handleTransaction(Scanner sc, int type) {
        System.out.print("Enter username: ");
        String name = sc.next();
        System.out.print("Enter amount: ");
        if (!sc.hasNextDouble()) {
            System.out.println("Invalid amount.");
            sc.next();
            return;
        }
        double amount = sc.nextDouble();

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        for (BankAccount acc : accounts) {
            if (acc.username.equalsIgnoreCase(name)) {
                if (type == 2) {
                    acc.balance += amount;
                    String record = "Deposit " + amount + " to " + name;
                    transactionHistory.push(record);
                    System.out.println(record);
                    System.out.println("New balance: " + acc.balance);
                } else {
                    if (acc.balance >= amount) {
                        acc.balance -= amount;
                        String record = "Withdraw " + amount + " from " + name;
                        transactionHistory.push(record);
                        System.out.println(record);
                        System.out.println("New balance: " + acc.balance);
                    } else {
                        System.out.println("Insufficient funds. Current balance: " + acc.balance);
                    }
                }
                return;
            }
        }
        System.out.println("Account not found.");
    }
}
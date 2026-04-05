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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Task 6 - Physical Structure (Array)
        System.out.println("--- Task 6: Physical Data Structure (Array) ---");
        BankAccount[] fixedAccounts = new BankAccount[3];
        fixedAccounts[0] = new BankAccount("101", "Ali", 150000);
        fixedAccounts[1] = new BankAccount("102", "Sara", 220000);
        fixedAccounts[2] = new BankAccount("103", "Ivan", 50000);
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
                    System.out.println("1-Request Opening, 2-Deposit, 3-Withdraw, 4-Undo");
                    int bChoice = sc.nextInt();
                    if (bChoice == 1) {
                        System.out.print("Name: ");
                        String name = sc.next();
                        accountRequests.add(new BankAccount("ACC"+(accounts.size()+1), name, 0));
                        System.out.println("Request added to queue.");
                    } else if (bChoice == 2 || bChoice == 3) {
                        handleTransaction(sc, bChoice);
                    } else if (bChoice == 4) {
                        if (!transactionHistory.isEmpty())
                            System.out.println("Undo: " + transactionHistory.pop() + " removed");
                    }
                    break;

                case 2: // ATM Menu
                    System.out.print("Username: ");
                    String user = sc.next();
                    for (BankAccount acc : accounts) {
                        if (acc.username.equalsIgnoreCase(user))
                            System.out.println("Balance: " + acc.balance);
                    }
                    break;

                case 3: // Admin Menu
                    System.out.println("1-Process Account, 2-Process Bill, 3-View All");
                    int aChoice = sc.nextInt();
                    if (aChoice == 1 && !accountRequests.isEmpty()) {
                        accounts.add(accountRequests.poll());
                        System.out.println("Account opened.");
                    } else if (aChoice == 2 && !billQueue.isEmpty()) {
                        System.out.println("Processed: " + billQueue.poll());
                    } else if (aChoice == 3) {
                        accounts.forEach(System.out::println);
                    }
                    break;
            }
        }
        sc.close();
    }

    private static void handleTransaction(Scanner sc, int type) {
        System.out.print("Username: ");
        String name = sc.next();
        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        for (BankAccount acc : accounts) {
            if (acc.username.equalsIgnoreCase(name)) {
                if (type == 2) {
                    acc.balance += amount;
                    transactionHistory.push("Deposit " + amount + " to " + name);
                } else if (acc.balance >= amount) {
                    acc.balance -= amount;
                    transactionHistory.push("Withdraw " + amount + " from " + name);
                }
                System.out.println("New balance: " + acc.balance);
                return;
            }
        }
        System.out.println("Account not found.");
    }
}
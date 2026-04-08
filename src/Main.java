import java.util.*;

class Node {
    Object data;
    Node next;

    Node(Object data) {
        this.data = data;
        this.next = null;
    }
}

class MyStack {
    private Node top;

    public void push(Object item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
    }

    public Object pop() {
        if (isEmpty()) return null;
        Object data = top.data;
        top = top.next;
        return data;
    }

    public Object peek() {
        return (top != null) ? top.data : null;
    }

    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "No transactions";
        String res = "";
        Node temp = top;
        while (temp != null) {
            res += "[" + temp.data + "] ";
            temp = temp.next;
        }
        return res;
    }
}

class MyQueue {
    private Node head, tail;

    public void add(Object item) {
        Node newNode = new Node(item);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) {
            head = newNode;
        }
    }

    public Object poll() {
        if (isEmpty()) return null;
        Object data = head.data;
        head = head.next;
        if (head == null) tail = null;
        return data;
    }

    public Object peek() {
        return (head != null) ? head.data : null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void display() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        Node temp = head;
        while (temp != null) {
            System.out.println("  - " + temp.data);
            temp = temp.next;
        }
    }
}

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
    static MyStack transactionHistory = new MyStack();
    static MyQueue billQueue = new MyQueue();
    static MyQueue accountRequests = new MyQueue();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
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
                case 1:
                    System.out.println("--- Bank Menu ---");
                    System.out.println("1-Request Account, 2-Deposit, 3-Withdraw, 4-Undo, 5-Peek, 6-Add Bill, 7-Search");
                    if (!sc.hasNextInt()) { sc.next(); break; }
                    int bChoice = sc.nextInt();

                    if (bChoice == 1) {
                        System.out.print("Enter your name: ");
                        String name = sc.next();
                        BankAccount req = new BankAccount("ACC" + (accounts.size() + 1), name, 0);
                        accountRequests.add(req);
                        System.out.println("Request added to queue.");

                    } else if (bChoice == 2 || bChoice == 3) {
                        handleTransaction(sc, bChoice);

                    } else if (bChoice == 4) {
                        if (!transactionHistory.isEmpty()) {
                            System.out.println("Undo: " + transactionHistory.pop() + " removed");
                        } else {
                            System.out.println("No transactions to undo.");
                        }

                    } else if (bChoice == 5) {
                        if (!transactionHistory.isEmpty()) {
                            System.out.println("Last transaction: " + transactionHistory.peek());
                        } else {
                            System.out.println("No transactions yet.");
                        }

                    } else if (bChoice == 6) {
                        System.out.print("Enter bill name: ");
                        String bill = sc.next();
                        billQueue.add(bill + " Bill");
                        transactionHistory.push("Bill payment: " + bill + " Bill");
                        System.out.println("Added to queue.");

                    } else if (bChoice == 7) {
                        System.out.print("Enter username: ");
                        String searchName = sc.next();
                        boolean found = false;
                        for (BankAccount acc : accounts) {
                            if (acc.username.equalsIgnoreCase(searchName)) {
                                System.out.println("Found: " + acc);
                                found = true;
                                break;
                            }
                        }
                        if (!found) System.out.println("Not found.");
                    }
                    break;
                case 2:
                    System.out.println("--- ATM Menu ---");
                    System.out.println("1-Balance, 2-Withdraw");
                    int atmChoice = sc.nextInt();
                    if (atmChoice == 1) {
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
                        if (!found) System.out.println("Not found.");
                    } else if (atmChoice == 2) {
                        handleTransaction(sc, 3);
                    }
                    break;

                case 3:
                    System.out.println("--- Admin Menu ---");
                    System.out.println("1-Process Req, 2-Process Bill, 3-View Accounts, 4-View Req Queue, 5-View Bill Queue");
                    int aChoice = sc.nextInt();

                    if (aChoice == 1) {
                        if (!accountRequests.isEmpty()) {
                            BankAccount newAcc = (BankAccount) accountRequests.poll();
                            accounts.add(newAcc);
                            System.out.println("Account opened for: " + newAcc.username);
                        } else {
                            System.out.println("No pending requests.");
                        }

                    } else if (aChoice == 2) {
                        if (!billQueue.isEmpty()) {
                            System.out.println("Processing: " + billQueue.poll());
                            if (!billQueue.isEmpty()) {
                                System.out.println("Remaining: " + billQueue.peek());
                            }
                        } else {
                            System.out.println("No bills.");
                        }

                    } else if (aChoice == 3) {
                        for (BankAccount acc : accounts) System.out.println(acc);

                    } else if (aChoice == 4) {
                        accountRequests.display();

                    } else if (aChoice == 5) {
                        billQueue.display();
                    }
                    break;
            }
        }
        sc.close();
    }

    private static void handleTransaction(Scanner sc, int type) {
        System.out.print("Enter username: ");
        String name = sc.next();
        System.out.print("Enter amount: ");
        if (!sc.hasNextDouble()) { sc.next(); return; }
        double amount = sc.nextDouble();

        for (BankAccount acc : accounts) {
            if (acc.username.equalsIgnoreCase(name)) {
                if (type == 2) {
                    acc.balance += amount;
                    transactionHistory.push("Deposit " + amount + " to " + name);
                    System.out.println("Success.");
                } else {
                    if (acc.balance >= amount) {
                        acc.balance -= amount;
                        transactionHistory.push("Withdraw " + amount + " from " + name);
                        System.out.println("Success.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                }
                return;
            }
        }
        System.out.println("Account not found.");
    }
}
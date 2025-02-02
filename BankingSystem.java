import java.io.*;
import java.util.*;

class Account implements Serializable {
    private int accountNumber;
    private String accountHolder;
    private double balance;

    public Account(int accountNumber, String accountHolder, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public int getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful! New Balance: " + balance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful! New Balance: " + balance);
            return true;
        } else {
            System.out.println("Insufficient balance or invalid amount.");
            return false;
        }
    }

    public void transfer(Account receiver, double amount) {
        if (this.withdraw(amount)) {
            receiver.deposit(amount);
            System.out.println("Transfer successful!");
        }
    }

    @Override
    public String toString() {
        return "Account No: " + accountNumber + ", Holder: " + accountHolder + ", Balance: $" + balance;
    }
}

public class BankingSystem {
    private List<Account> accounts;
    private final String FILE_NAME = "accounts.txt";

    public BankingSystem() {
        accounts = new ArrayList<>();
        loadAccounts();
    }

    public void createAccount(int accountNumber, String holderName, double initialBalance) {
        accounts.add(new Account(accountNumber, holderName, initialBalance));
        saveAccounts();
    }

    public Account getAccount(int accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    public void displayAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            accounts.forEach(System.out::println);
        }
    }

    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    private void loadAccounts() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts.");
        }
    }

    public static void main(String[] args) {
        BankingSystem bank = new BankingSystem();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Create Account");
            System.out.println("2. View Accounts");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer Funds");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Account Number: ");
                    int accNo = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Holder Name: ");
                    String holder = sc.nextLine();
                    System.out.print("Enter Initial Balance: ");
                    double balance = sc.nextDouble();
                    bank.createAccount(accNo, holder, balance);
                    break;
                case 2:
                    bank.displayAccounts();
                    break;
                case 3:
                    System.out.print("Enter Account Number: ");
                    int depAcc = sc.nextInt();
                    System.out.print("Enter Amount to Deposit: ");
                    double depAmount = sc.nextDouble();
                    Account depAccount = bank.getAccount(depAcc);
                    if (depAccount != null) depAccount.deposit(depAmount);
                    else System.out.println("Account not found.");
                    break;
                case 4:
                    System.out.print("Enter Account Number: ");
                    int withAcc = sc.nextInt();
                    System.out.print("Enter Amount to Withdraw: ");
                    double withAmount = sc.nextDouble();
                    Account withAccount = bank.getAccount(withAcc);
                    if (withAccount != null) withAccount.withdraw(withAmount);
                    else System.out.println("Account not found.");
                    break;
                case 5:
                    System.out.print("Enter Sender Account Number: ");
                    int senderAcc = sc.nextInt();
                    System.out.print("Enter Receiver Account Number: ");
                    int receiverAcc = sc.nextInt();
                    System.out.print("Enter Amount to Transfer: ");
                    double transAmount = sc.nextDouble();

                    Account sender = bank.getAccount(senderAcc);
                    Account receiver = bank.getAccount(receiverAcc);

                    if (sender != null && receiver != null) {
                        sender.transfer(receiver, transAmount);
                    } else {
                        System.out.println("Invalid account details.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}

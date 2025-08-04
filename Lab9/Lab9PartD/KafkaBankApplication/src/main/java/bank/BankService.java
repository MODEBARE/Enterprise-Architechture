package bank;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BankService {
    
    // In-memory storage for demo purposes
    private Map<String, Account> accounts = new HashMap<>();
    
    public void createAccount(String accountNumber, String customerName, double initialBalance) {
        if (accounts.containsKey(accountNumber)) {
            System.out.println("❌ Error: Account " + accountNumber + " already exists!");
            return;
        }
        
        Account account = new Account(accountNumber, customerName, initialBalance);
        accounts.put(accountNumber, account);
        
        System.out.println("✅ ACCOUNT CREATED:");
        System.out.println("   Account Number: " + accountNumber);
        System.out.println("   Customer Name: " + customerName);
        System.out.println("   Initial Balance: $" + initialBalance);
        System.out.println();
    }
    
    public void deposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("❌ Error: Account " + accountNumber + " not found!");
            return;
        }
        
        if (amount <= 0) {
            System.out.println("❌ Error: Deposit amount must be positive!");
            return;
        }
        
        double oldBalance = account.getBalance();
        account.setBalance(oldBalance + amount);
        
        System.out.println("💰 DEPOSIT SUCCESSFUL:");
        System.out.println("   Account Number: " + accountNumber);
        System.out.println("   Customer: " + account.getCustomerName());
        System.out.println("   Deposit Amount: $" + amount);
        System.out.println("   Previous Balance: $" + oldBalance);
        System.out.println("   New Balance: $" + account.getBalance());
        System.out.println();
    }
    
    public void withdraw(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("❌ Error: Account " + accountNumber + " not found!");
            return;
        }
        
        if (amount <= 0) {
            System.out.println("❌ Error: Withdrawal amount must be positive!");
            return;
        }
        
        if (account.getBalance() < amount) {
            System.out.println("❌ Error: Insufficient funds! Available balance: $" + account.getBalance());
            return;
        }
        
        double oldBalance = account.getBalance();
        account.setBalance(oldBalance - amount);
        
        System.out.println("💸 WITHDRAWAL SUCCESSFUL:");
        System.out.println("   Account Number: " + accountNumber);
        System.out.println("   Customer: " + account.getCustomerName());
        System.out.println("   Withdrawal Amount: $" + amount);
        System.out.println("   Previous Balance: $" + oldBalance);
        System.out.println("   New Balance: $" + account.getBalance());
        System.out.println();
    }
    
    public void printAllAccounts() {
        System.out.println("=== ALL BANK ACCOUNTS ===");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : accounts.values()) {
                System.out.println(account);
            }
        }
        System.out.println("=========================");
        System.out.println();
    }
} 
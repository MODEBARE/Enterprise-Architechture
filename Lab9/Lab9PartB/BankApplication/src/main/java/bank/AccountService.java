package bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    // In-memory storage for demo purposes
    private Map<String, Account> accounts = new HashMap<>();
    
    // Tax reporting threshold
    private static final double TAX_REPORTING_THRESHOLD = 10000.0;
    private static final String TAX_REPORTING_CURRENCY = "EUR";
    
    public AccountService() {
        // Initialize some demo accounts
        accounts.put("ACC001", new Account("ACC001", "John Doe", 5000.0));
        accounts.put("ACC002", new Account("ACC002", "Jane Smith", 15000.0));
        accounts.put("ACC003", new Account("ACC003", "Bob Johnson", 2500.0));
    }
    
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public void makeDeposit(String accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Error: Account " + accountNumber + " not found!");
            return;
        }
        
        // Update account balance
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        
        System.out.println("=== BANK TRANSACTION ===");
        System.out.println("Deposit processed for account: " + accountNumber);
        System.out.println("Customer: " + account.getCustomerName());
        System.out.println("Deposit amount: " + amount + " EUR");
        System.out.println("New balance: " + newBalance + " EUR");
        
        // Check if deposit requires tax reporting
        if (amount >= TAX_REPORTING_THRESHOLD) {
            System.out.println("*** Large deposit detected - Notifying Tax Service ***");
            sendTaxNotification(accountNumber, amount);
        }
        
        System.out.println("========================");
    }
    
    private void sendTaxNotification(String accountNumber, double amount) {
        try {
            // Create tax notification message
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            DepositMessage taxMessage = new DepositMessage(accountNumber, amount, TAX_REPORTING_CURRENCY, timestamp);
            
            // Convert to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String messageAsString = objectMapper.writeValueAsString(taxMessage);
            
            // Send JMS message to tax service
            jmsTemplate.convertAndSend("taxQueue", messageAsString);
            System.out.println("Tax notification sent to TaxService: " + messageAsString);
            
        } catch (Exception e) {
            System.out.println("Error sending tax notification: " + e.getMessage());
        }
    }
    
    public void printAllAccounts() {
        System.out.println("=== ALL BANK ACCOUNTS ===");
        for (Account account : accounts.values()) {
            System.out.println(account);
        }
        System.out.println("=========================");
    }
} 
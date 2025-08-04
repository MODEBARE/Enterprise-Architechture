package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BankClientApplication implements CommandLineRunner {

    @Autowired
    private BankClientService bankClientService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BankClientApplication.class, args);
        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("💳 KAFKA BANK CLIENT APPLICATION STARTED");
        System.out.println("==========================================");
        System.out.println("Testing all bank operations via Kafka messaging...");
        System.out.println();

        // Wait a moment for Kafka to be ready
        Thread.sleep(2000);

        // Test 1: Create Account Operations
        System.out.println("📋 TEST 1: Creating new accounts");
        System.out.println("─────────────────────────────────");
        bankClientService.createAccount("ACC001", "Alice Johnson", 1000.0);
        Thread.sleep(1000);
        
        bankClientService.createAccount("ACC002", "Bob Smith", 2500.0);
        Thread.sleep(1000);
        
        bankClientService.createAccount("ACC003", "Carol Davis", 500.0);
        Thread.sleep(1000);

        // Test 2: Deposit Operations
        System.out.println("📋 TEST 2: Making deposits");
        System.out.println("──────────────────────────");
        bankClientService.deposit("ACC001", 500.0);
        Thread.sleep(1000);
        
        bankClientService.deposit("ACC002", 1000.0);
        Thread.sleep(1000);
        
        bankClientService.deposit("ACC003", 250.0);
        Thread.sleep(1000);

        // Test 3: Withdrawal Operations
        System.out.println("📋 TEST 3: Making withdrawals");
        System.out.println("─────────────────────────────");
        bankClientService.withdraw("ACC001", 200.0);
        Thread.sleep(1000);
        
        bankClientService.withdraw("ACC002", 500.0);
        Thread.sleep(1000);
        
        bankClientService.withdraw("ACC003", 100.0);
        Thread.sleep(1000);

        // Test 4: Error Conditions
        System.out.println("📋 TEST 4: Testing error conditions");
        System.out.println("───────────────────────────────────");
        // Try to deposit to non-existent account
        bankClientService.deposit("ACC999", 100.0);
        Thread.sleep(1000);
        
        // Try to withdraw more than available balance
        bankClientService.withdraw("ACC003", 1000.0);
        Thread.sleep(1000);
        
        // Try to create duplicate account
        bankClientService.createAccount("ACC001", "Duplicate User", 100.0);
        Thread.sleep(1000);

        System.out.println("==========================================");
        System.out.println("✅ BANK CLIENT DEMO COMPLETED");
        System.out.println("Check the Bank Application console to see the processed operations!");
        System.out.println();
    }
} 
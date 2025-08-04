package bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class BankApplication implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BankApplication.class, args);
        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== BANK APPLICATION STARTED ===");
        System.out.println("Testing deposit functionality with JMS tax reporting...");
        System.out.println();

        // Show initial account balances
        accountService.printAllAccounts();
        System.out.println();

        // Test small deposit (below threshold) - should not trigger tax notification
        System.out.println("Testing small deposit (5000 EUR) - should NOT trigger tax notification:");
        accountService.makeDeposit("ACC001", 5000.0);
        System.out.println();

        // Test medium deposit (below threshold) - should not trigger tax notification
        System.out.println("Testing medium deposit (7500 EUR) - should NOT trigger tax notification:");
        accountService.makeDeposit("ACC003", 7500.0);
        System.out.println();

        // Test large deposit (above threshold) - should trigger tax notification
        System.out.println("Testing large deposit (15000 EUR) - should trigger tax notification:");
        accountService.makeDeposit("ACC002", 15000.0);
        System.out.println();

        // Test another large deposit (exactly at threshold) - should trigger tax notification
        System.out.println("Testing exact threshold deposit (10000 EUR) - should trigger tax notification:");
        accountService.makeDeposit("ACC001", 10000.0);
        System.out.println();

        // Show final account balances
        System.out.println("Final account balances:");
        accountService.printAllAccounts();
        
        System.out.println("=== BANK APPLICATION DEMO COMPLETED ===");
        System.out.println("Check TaxService console for tax notifications!");
    }
} 
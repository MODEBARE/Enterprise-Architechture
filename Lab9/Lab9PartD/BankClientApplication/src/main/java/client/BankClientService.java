package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BankClientService {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String BANK_OPERATIONS_TOPIC = "bankOperations";
    
    public void createAccount(String accountNumber, String customerName, double initialBalance) {
        try {
            BankOperation operation = new BankOperation("CREATE_ACCOUNT", accountNumber, customerName, initialBalance);
            String message = objectMapper.writeValueAsString(operation);
            
            System.out.println("📤 Sending CREATE_ACCOUNT request:");
            System.out.println("   Account Number: " + accountNumber);
            System.out.println("   Customer Name: " + customerName);
            System.out.println("   Initial Balance: $" + initialBalance);
            
            kafkaTemplate.send(BANK_OPERATIONS_TOPIC, message);
            System.out.println("✅ Request sent to bank via Kafka");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("❌ Error sending create account request: " + e.getMessage());
        }
    }
    
    public void deposit(String accountNumber, double amount) {
        try {
            BankOperation operation = new BankOperation("DEPOSIT", accountNumber, null, amount);
            String message = objectMapper.writeValueAsString(operation);
            
            System.out.println("📤 Sending DEPOSIT request:");
            System.out.println("   Account Number: " + accountNumber);
            System.out.println("   Amount: $" + amount);
            
            kafkaTemplate.send(BANK_OPERATIONS_TOPIC, message);
            System.out.println("✅ Request sent to bank via Kafka");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("❌ Error sending deposit request: " + e.getMessage());
        }
    }
    
    public void withdraw(String accountNumber, double amount) {
        try {
            BankOperation operation = new BankOperation("WITHDRAW", accountNumber, null, amount);
            String message = objectMapper.writeValueAsString(operation);
            
            System.out.println("📤 Sending WITHDRAW request:");
            System.out.println("   Account Number: " + accountNumber);
            System.out.println("   Amount: $" + amount);
            
            kafkaTemplate.send(BANK_OPERATIONS_TOPIC, message);
            System.out.println("✅ Request sent to bank via Kafka");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("❌ Error sending withdrawal request: " + e.getMessage());
        }
    }
} 
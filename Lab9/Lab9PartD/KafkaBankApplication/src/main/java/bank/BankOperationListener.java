package bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BankOperationListener {

    @Autowired
    private BankService bankService;

    @KafkaListener(topics = "bankOperations", groupId = "bank-group")
    public void handleBankOperation(String operationMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BankOperation operation = objectMapper.readValue(operationMessage, BankOperation.class);
            
            System.out.println("🏦 Received bank operation: " + operation.getOperationType());
            
            switch (operation.getOperationType()) {
                case "CREATE_ACCOUNT":
                    bankService.createAccount(
                        operation.getAccountNumber(), 
                        operation.getCustomerName(), 
                        operation.getAmount()
                    );
                    break;
                    
                case "DEPOSIT":
                    bankService.deposit(operation.getAccountNumber(), operation.getAmount());
                    break;
                    
                case "WITHDRAW":
                    bankService.withdraw(operation.getAccountNumber(), operation.getAmount());
                    break;
                    
                default:
                    System.out.println("❌ Unknown operation type: " + operation.getOperationType());
            }
            
        } catch (IOException e) {
            System.out.println("❌ Error processing bank operation: " + e.getMessage());
            System.out.println("   Original message: " + operationMessage);
        }
    }
} 
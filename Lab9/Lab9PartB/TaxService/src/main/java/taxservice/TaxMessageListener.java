package taxservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TaxMessageListener {

    @JmsListener(destination = "taxQueue")
    public void receiveDepositMessage(final String depositMessageAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            DepositMessage depositMessage = objectMapper.readValue(depositMessageAsString, DepositMessage.class);
            
            System.out.println("=== TAX SERVICE NOTIFICATION ===");
            System.out.println("Large deposit detected requiring tax reporting:");
            System.out.println("Account Number: " + depositMessage.getAccountNumber());
            System.out.println("Deposit Amount: " + depositMessage.getAmount() + " " + depositMessage.getCurrency());
            System.out.println("Timestamp: " + depositMessage.getTimestamp());
            System.out.println("Tax reporting initiated for this deposit.");
            System.out.println("==============================");
            
        } catch (IOException e) {
            System.out.println("TaxService: Cannot convert message to DepositMessage object: " + depositMessageAsString);
            System.out.println("Error: " + e.getMessage());
        }
    }
} 
package jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
@EnableJms
public class CalculatorSenderApplication implements CommandLineRunner {
    @Autowired
    JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CalculatorSenderApplication.class, args);
        context.close();
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Send + operation
        CalculatorMessage addMessage = new CalculatorMessage("+", 7);
        String addMessageAsString = objectMapper.writeValueAsString(addMessage);
        System.out.println("Sending calculator command: " + addMessageAsString);
        jmsTemplate.convertAndSend("calculatorTopic", addMessageAsString);

        // Send * operation  
        CalculatorMessage multiplyMessage = new CalculatorMessage("*", 8);
        String multiplyMessageAsString = objectMapper.writeValueAsString(multiplyMessage);
        System.out.println("Sending calculator command: " + multiplyMessageAsString);
        jmsTemplate.convertAndSend("calculatorTopic", multiplyMessageAsString);
    }
} 
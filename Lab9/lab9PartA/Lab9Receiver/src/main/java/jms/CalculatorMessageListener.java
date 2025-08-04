package jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CalculatorMessageListener {
    private int currentValue = 0; // Starting value for calculator

    @JmsListener(destination = "calculatorTopic")
    public void receiveMessage(final String calculatorMessageAsString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CalculatorMessage message = objectMapper.readValue(calculatorMessageAsString, CalculatorMessage.class);
            
            System.out.println("Calculator receiver received command: " + message.getOperator() + " " + message.getValue());
            
            // Perform the calculation
            switch (message.getOperator()) {
                case "+":
                    currentValue += message.getValue();
                    break;
                case "-":
                    currentValue -= message.getValue();
                    break;
                case "*":
                    currentValue *= message.getValue();
                    break;
                case "/":
                    if (message.getValue() != 0) {
                        currentValue /= message.getValue();
                    } else {
                        System.out.println("Calculator error: Division by zero!");
                        return;
                    }
                    break;
                default:
                    System.out.println("Calculator error: Unknown operator " + message.getOperator());
                    return;
            }
            
            System.out.println("Calculator result: " + currentValue);
            
        } catch (IOException e) {
            System.out.println("Calculator receiver: Cannot convert : " + calculatorMessageAsString + " to a CalculatorMessage object");
        }
    }
} 
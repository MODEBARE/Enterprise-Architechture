package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Lab12ClientApplication {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Lab12ClientApplication.class, args);
        
        // Get the client service and run tests
        Lab12RestClient client = context.getBean(Lab12RestClient.class);
        client.testAllEndpoints();
        
        // Close the application context
        context.close();
    }
}
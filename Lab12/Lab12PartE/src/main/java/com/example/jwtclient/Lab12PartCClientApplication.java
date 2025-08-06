package com.example.jwtclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Lab12PartCClientApplication {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Lab12PartCClientApplication.class, args);
        
        // Get the JWT client service and run tests
        JwtRestClient client = context.getBean(JwtRestClient.class);
        client.testAllEndpoints();
        
        // Close the application context
        context.close();
    }
}
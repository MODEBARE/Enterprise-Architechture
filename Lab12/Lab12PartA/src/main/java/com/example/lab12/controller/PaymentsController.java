package com.example.lab12.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentsController {

    @GetMapping("/payments")
    public String getPayments(Authentication authentication) {
        return "Payments endpoint - accessible only by finance department employees. " +
               "Current user: " + authentication.getName() + 
               " with authorities: " + authentication.getAuthorities();
    }
}
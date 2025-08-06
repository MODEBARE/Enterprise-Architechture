package com.example.lab12.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrdersController {

    @GetMapping("/orders")
    public String getOrders(Authentication authentication) {
        return "Orders endpoint - accessible by all employees. " +
               "Current user: " + authentication.getName() + 
               " with authorities: " + authentication.getAuthorities();
    }
}
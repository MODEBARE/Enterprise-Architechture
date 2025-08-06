package com.example.lab12.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopController {

    @GetMapping("/shop")
    public String getShop() {
        return "Welcome to the Shop! This endpoint is accessible by everyone.";
    }
}
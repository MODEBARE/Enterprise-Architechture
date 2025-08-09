package com.profitsystem.currency.controller;

import com.profitsystem.currency.dto.ConversionRequest;
import com.profitsystem.currency.dto.ConversionResponse;
import com.profitsystem.currency.service.CurrencyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "*")
public class CurrencyController {
    
    @Autowired
    private CurrencyService currencyService;
    
    @PostMapping("/convert")
    public ResponseEntity<ConversionResponse> convertCurrency(@Valid @RequestBody ConversionRequest request) {
        try {
            ConversionResponse response = currencyService.convertCurrency(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/convert")
    public ResponseEntity<ConversionResponse> convertCurrencyGet(
            @RequestParam BigDecimal amount,
            @RequestParam String from,
            @RequestParam String to) {
        try {
            ConversionRequest request = new ConversionRequest(amount, from, to);
            ConversionResponse response = currencyService.convertCurrency(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/supported")
    public ResponseEntity<Map<String, BigDecimal>> getSupportedCurrencies() {
        Map<String, BigDecimal> currencies = currencyService.getSupportedCurrencies();
        return ResponseEntity.ok(currencies);
    }
    
    @GetMapping("/rate")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        try {
            BigDecimal rate = currencyService.getExchangeRate(from, to);
            return ResponseEntity.ok(rate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Currency Service is running");
    }
}
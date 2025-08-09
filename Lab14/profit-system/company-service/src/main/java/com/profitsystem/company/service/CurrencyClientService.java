package com.profitsystem.company.service;

import com.profitsystem.company.dto.ConversionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CurrencyClientService {
    
    private static final Logger log = LoggerFactory.getLogger(CurrencyClientService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${currency.service.url:http://localhost:8082}")
    private String currencyServiceUrl;
    
    public Optional<ConversionResponse> convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        try {
            String url = String.format("%s/api/currency/convert?amount=%s&from=%s&to=%s", 
                currencyServiceUrl, amount, fromCurrency, toCurrency);
            log.info("Calling currency service: {}", url);
            
            ResponseEntity<ConversionResponse> response = restTemplate.getForEntity(url, ConversionResponse.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error calling currency service: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String url = String.format("%s/api/currency/rate?from=%s&to=%s", 
                currencyServiceUrl, fromCurrency, toCurrency);
            log.info("Calling currency service: {}", url);
            
            ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error getting exchange rate: {}", e.getMessage());
            return BigDecimal.ONE;
        }
    }
}
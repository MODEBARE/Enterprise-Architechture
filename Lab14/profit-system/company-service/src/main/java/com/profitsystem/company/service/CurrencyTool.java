package com.profitsystem.company.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.Function;

@Service
public class CurrencyTool implements Function<CurrencyTool.Request, String> {
    
    private static final Logger log = LoggerFactory.getLogger(CurrencyTool.class);
    
    @Autowired
    private CurrencyClientService currencyClientService;
    
    public record Request(BigDecimal amount, String fromCurrency, String toCurrency) {}
    
    @Override
    public String apply(Request request) {
        log.info("CurrencyTool called with: {}", request);
        
        try {
            var conversion = currencyClientService.convertCurrency(
                request.amount(), 
                request.fromCurrency(), 
                request.toCurrency()
            );
            
            if (conversion.isPresent()) {
                var result = conversion.get();
                return String.format("Converted %s %s to %s %s (rate: %s)", 
                    result.getOriginalAmount(), 
                    result.getFromCurrency(),
                    result.getConvertedAmount(), 
                    result.getToCurrency(),
                    result.getExchangeRate());
            } else {
                return "Currency conversion failed. Please check the currency codes.";
            }
            
        } catch (Exception e) {
            log.error("Error in CurrencyTool: {}", e.getMessage());
            return "Error converting currency: " + e.getMessage();
        }
    }
}
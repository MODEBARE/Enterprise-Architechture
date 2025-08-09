package com.profitsystem.currency.service;

import com.profitsystem.currency.dto.ConversionRequest;
import com.profitsystem.currency.dto.ConversionResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyService {
    
    // Mock exchange rates (in production, this would come from a real API)
    private final Map<String, BigDecimal> exchangeRates;
    
    public CurrencyService() {
        exchangeRates = new HashMap<>();
        initializeExchangeRates();
    }
    
    private void initializeExchangeRates() {
        // Base currency: USD
        exchangeRates.put("USD", BigDecimal.ONE);
        exchangeRates.put("EUR", new BigDecimal("0.85"));
        exchangeRates.put("GBP", new BigDecimal("0.73"));
        exchangeRates.put("CAD", new BigDecimal("1.35"));
        exchangeRates.put("AUD", new BigDecimal("1.52"));
        exchangeRates.put("JPY", new BigDecimal("110.25"));
        exchangeRates.put("CHF", new BigDecimal("0.92"));
        exchangeRates.put("CNY", new BigDecimal("6.45"));
        exchangeRates.put("INR", new BigDecimal("74.50"));
        exchangeRates.put("BRL", new BigDecimal("5.20"));
        exchangeRates.put("MXN", new BigDecimal("20.15"));
        exchangeRates.put("KRW", new BigDecimal("1180.75"));
    }
    
    public ConversionResponse convertCurrency(ConversionRequest request) {
        String fromCurrency = request.getFromCurrency().toUpperCase();
        String toCurrency = request.getToCurrency().toUpperCase();
        
        if (!exchangeRates.containsKey(fromCurrency)) {
            throw new IllegalArgumentException("Unsupported from currency: " + fromCurrency);
        }
        
        if (!exchangeRates.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported to currency: " + toCurrency);
        }
        
        if (fromCurrency.equals(toCurrency)) {
            return new ConversionResponse(
                request.getAmount(),
                fromCurrency,
                request.getAmount(),
                toCurrency,
                BigDecimal.ONE,
                getCurrentTimestamp()
            );
        }
        
        // Convert to USD first, then to target currency
        BigDecimal fromRate = exchangeRates.get(fromCurrency);
        BigDecimal toRate = exchangeRates.get(toCurrency);
        
        // Calculate exchange rate: (1/fromRate) * toRate
        BigDecimal exchangeRate = BigDecimal.ONE.divide(fromRate, 10, RoundingMode.HALF_UP)
                .multiply(toRate)
                .setScale(6, RoundingMode.HALF_UP);
        
        BigDecimal convertedAmount = request.getAmount()
                .multiply(exchangeRate)
                .setScale(2, RoundingMode.HALF_UP);
        
        return new ConversionResponse(
            request.getAmount(),
            fromCurrency,
            convertedAmount,
            toCurrency,
            exchangeRate,
            getCurrentTimestamp()
        );
    }
    
    public Map<String, BigDecimal> getSupportedCurrencies() {
        return new HashMap<>(exchangeRates);
    }
    
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String from = fromCurrency.toUpperCase();
        String to = toCurrency.toUpperCase();
        
        if (!exchangeRates.containsKey(from) || !exchangeRates.containsKey(to)) {
            throw new IllegalArgumentException("Unsupported currency");
        }
        
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }
        
        BigDecimal fromRate = exchangeRates.get(from);
        BigDecimal toRate = exchangeRates.get(to);
        
        return BigDecimal.ONE.divide(fromRate, 10, RoundingMode.HALF_UP)
                .multiply(toRate)
                .setScale(6, RoundingMode.HALF_UP);
    }
    
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
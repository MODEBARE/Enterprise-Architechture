package com.profitsystem.company.service;

import com.profitsystem.company.dto.ProfitResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ProfitTool implements Function<ProfitTool.Request, String> {
    
    private static final Logger log = LoggerFactory.getLogger(ProfitTool.class);
    
    @Autowired
    private ProfitClientService profitClientService;
    
    @Autowired
    private CurrencyClientService currencyClientService;
    
    public record Request(String month, Integer year, String type, String currency) {}
    
    @Override
    public String apply(Request request) {
        log.info("ProfitTool called with: {}", request);
        
        try {
            String currency = request.currency() != null ? request.currency() : "USD";
            
            if ("monthly".equals(request.type()) && request.month() != null) {
                return getMonthlyProfit(request.month(), currency);
            } else if ("yearly".equals(request.type()) && request.year() != null) {
                return getYearlyProfit(request.year(), currency);
            } else if ("all".equals(request.type())) {
                return getAllProfits(currency);
            } else if ("best".equals(request.type())) {
                return getBestMonth(currency);
            } else if ("worst".equals(request.type())) {
                return getWorstMonth(currency);
            }
            
            return "Invalid request parameters. Please specify type (monthly, yearly, all, best, worst) and appropriate parameters.";
            
        } catch (Exception e) {
            log.error("Error in ProfitTool: {}", e.getMessage());
            return "Error retrieving profit data: " + e.getMessage();
        }
    }
    
    private String getMonthlyProfit(String month, String currency) {
        Optional<ProfitResponseDto> profit = profitClientService.getProfitByMonth(month);
        if (profit.isPresent()) {
            BigDecimal profitAmount = profit.get().getProfit();
            
            if (!currency.equalsIgnoreCase("USD")) {
                var conversion = currencyClientService.convertCurrency(profitAmount, "USD", currency);
                if (conversion.isPresent()) {
                    profitAmount = conversion.get().getConvertedAmount();
                }
            }
            
            return String.format("Profit for %s: %s %s (%s)", 
                month, currency, profitAmount, profit.get().getDescription());
        }
        return "No profit data found for " + month;
    }
    
    private String getYearlyProfit(Integer year, String currency) {
        BigDecimal totalProfit = profitClientService.getTotalProfitForYear(year);
        
        if (!currency.equalsIgnoreCase("USD")) {
            var conversion = currencyClientService.convertCurrency(totalProfit, "USD", currency);
            if (conversion.isPresent()) {
                totalProfit = conversion.get().getConvertedAmount();
            }
        }
        
        return String.format("Total profit for %d: %s %s", year, currency, totalProfit);
    }
    
    private String getAllProfits(String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        BigDecimal total = allProfits.stream()
            .map(ProfitResponseDto::getProfit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (!currency.equalsIgnoreCase("USD")) {
            var conversion = currencyClientService.convertCurrency(total, "USD", currency);
            if (conversion.isPresent()) {
                total = conversion.get().getConvertedAmount();
            }
        }
        
        return String.format("Total profit across all months: %s %s (covering %d months)", 
            currency, total, allProfits.size());
    }
    
    private String getBestMonth(String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        Optional<ProfitResponseDto> best = allProfits.stream()
            .max((p1, p2) -> p1.getProfit().compareTo(p2.getProfit()));
        
        if (best.isPresent()) {
            BigDecimal profitAmount = best.get().getProfit();
            
            if (!currency.equalsIgnoreCase("USD")) {
                var conversion = currencyClientService.convertCurrency(profitAmount, "USD", currency);
                if (conversion.isPresent()) {
                    profitAmount = conversion.get().getConvertedAmount();
                }
            }
            
            return String.format("Best performing month: %s with %s %s (%s)", 
                best.get().getMonth(), currency, profitAmount, best.get().getDescription());
        }
        return "No profit data available";
    }
    
    private String getWorstMonth(String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        Optional<ProfitResponseDto> worst = allProfits.stream()
            .min((p1, p2) -> p1.getProfit().compareTo(p2.getProfit()));
        
        if (worst.isPresent()) {
            BigDecimal profitAmount = worst.get().getProfit();
            
            if (!currency.equalsIgnoreCase("USD")) {
                var conversion = currencyClientService.convertCurrency(profitAmount, "USD", currency);
                if (conversion.isPresent()) {
                    profitAmount = conversion.get().getConvertedAmount();
                }
            }
            
            return String.format("Worst performing month: %s with %s %s (%s)", 
                worst.get().getMonth(), currency, profitAmount, worst.get().getDescription());
        }
        return "No profit data available";
    }
}
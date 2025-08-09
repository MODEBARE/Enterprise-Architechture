package com.profitsystem.company.service;

import com.profitsystem.company.dto.ConversionResponse;
import com.profitsystem.company.dto.ProfitResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LLMService {
    
    private static final Logger log = LoggerFactory.getLogger(LLMService.class);
    
    @Autowired
    private ProfitClientService profitClientService;
    
    @Autowired
    private CurrencyClientService currencyClientService;
    
    public String processQuestion(String question, String currency) {
        log.info("Processing question: {} in currency: {}", question, currency);
        
        String lowerQuestion = question.toLowerCase();
        
        // Detect question type and extract parameters
        if (lowerQuestion.contains("profit") && (lowerQuestion.contains("month") || lowerQuestion.contains("january") || 
            lowerQuestion.contains("february") || lowerQuestion.contains("march") || lowerQuestion.contains("april") ||
            lowerQuestion.contains("may") || lowerQuestion.contains("june") || lowerQuestion.contains("july") ||
            lowerQuestion.contains("august") || lowerQuestion.contains("september") || lowerQuestion.contains("october") ||
            lowerQuestion.contains("november") || lowerQuestion.contains("december"))) {
            
            return handleMonthlyProfitQuestion(question, currency);
            
        } else if (lowerQuestion.contains("profit") && lowerQuestion.contains("year")) {
            
            return handleYearlyProfitQuestion(question, currency);
            
        } else if (lowerQuestion.contains("profit") && (lowerQuestion.contains("all") || lowerQuestion.contains("total") || 
                  lowerQuestion.contains("overall") || lowerQuestion.contains("complete"))) {
            
            return handleOverallProfitQuestion(question, currency);
            
        } else if (lowerQuestion.contains("best") || lowerQuestion.contains("highest") || lowerQuestion.contains("maximum")) {
            
            return handleBestPerformanceQuestion(question, currency);
            
        } else if (lowerQuestion.contains("worst") || lowerQuestion.contains("lowest") || lowerQuestion.contains("minimum")) {
            
            return handleWorstPerformanceQuestion(question, currency);
            
        } else {
            return "I can help you with profit-related questions. You can ask about:\n" +
                   "- Monthly profits (e.g., 'What was the profit in January 2024?')\n" +
                   "- Yearly profits (e.g., 'What was the total profit for 2023?')\n" +
                   "- Best/worst performing months\n" +
                   "- Overall profit summaries\n" +
                   "Please rephrase your question to include these topics.";
        }
    }
    
    private String handleMonthlyProfitQuestion(String question, String currency) {
        String month = extractMonth(question);
        if (month != null) {
            Optional<ProfitResponseDto> profit = profitClientService.getProfitByMonth(month);
            if (profit.isPresent()) {
                BigDecimal profitAmount = profit.get().getProfit();
                String description = profit.get().getDescription();
                
                if (!currency.equalsIgnoreCase("USD")) {
                    Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                        profitAmount, "USD", currency);
                    if (conversion.isPresent()) {
                        profitAmount = conversion.get().getConvertedAmount();
                        return String.format("The profit for %s was %s %s (%s). %s", 
                            formatMonth(month), currency, profitAmount, description,
                            "Converted from USD at rate " + conversion.get().getExchangeRate());
                    }
                }
                
                return String.format("The profit for %s was %s %s (%s).", 
                    formatMonth(month), currency, profitAmount, description);
            } else {
                return "No profit data found for " + formatMonth(month) + ".";
            }
        } else {
            return "I couldn't identify the specific month from your question. Please specify a month and year (e.g., 'January 2024').";
        }
    }
    
    private String handleYearlyProfitQuestion(String question, String currency) {
        Integer year = extractYear(question);
        if (year != null) {
            BigDecimal totalProfit = profitClientService.getTotalProfitForYear(year);
            List<ProfitResponseDto> monthlyProfits = profitClientService.getProfitsByYear(year);
            
            if (!currency.equalsIgnoreCase("USD")) {
                Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                    totalProfit, "USD", currency);
                if (conversion.isPresent()) {
                    totalProfit = conversion.get().getConvertedAmount();
                }
            }
            
            StringBuilder response = new StringBuilder();
            response.append(String.format("Total profit for %d was %s %s.\n\n", year, currency, totalProfit));
            response.append("Monthly breakdown:\n");
            
            for (ProfitResponseDto monthlyProfit : monthlyProfits) {
                BigDecimal monthAmount = monthlyProfit.getProfit();
                if (!currency.equalsIgnoreCase("USD")) {
                    Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                        monthAmount, "USD", currency);
                    if (conversion.isPresent()) {
                        monthAmount = conversion.get().getConvertedAmount();
                    }
                }
                response.append(String.format("- %s: %s %s\n", 
                    monthlyProfit.getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    currency, monthAmount));
            }
            
            return response.toString();
        } else {
            return "I couldn't identify the specific year from your question. Please specify a year (e.g., '2023' or '2024').";
        }
    }
    
    private String handleOverallProfitQuestion(String question, String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        
        if (allProfits.isEmpty()) {
            return "No profit data available.";
        }
        
        BigDecimal totalProfit = allProfits.stream()
            .map(ProfitResponseDto::getProfit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (!currency.equalsIgnoreCase("USD")) {
            Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                totalProfit, "USD", currency);
            if (conversion.isPresent()) {
                totalProfit = conversion.get().getConvertedAmount();
            }
        }
        
        return String.format("Total profit across all recorded months is %s %s. " +
            "This covers %d months of data from %s to %s.",
            currency, totalProfit, allProfits.size(),
            allProfits.get(allProfits.size() - 1).getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            allProfits.get(0).getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy")));
    }
    
    private String handleBestPerformanceQuestion(String question, String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        
        if (allProfits.isEmpty()) {
            return "No profit data available.";
        }
        
        Optional<ProfitResponseDto> bestMonth = allProfits.stream()
            .max((p1, p2) -> p1.getProfit().compareTo(p2.getProfit()));
        
        if (bestMonth.isPresent()) {
            BigDecimal bestProfit = bestMonth.get().getProfit();
            
            if (!currency.equalsIgnoreCase("USD")) {
                Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                    bestProfit, "USD", currency);
                if (conversion.isPresent()) {
                    bestProfit = conversion.get().getConvertedAmount();
                }
            }
            
            return String.format("The best performing month was %s with a profit of %s %s (%s).",
                bestMonth.get().getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                currency, bestProfit, bestMonth.get().getDescription());
        }
        
        return "Could not determine the best performing month.";
    }
    
    private String handleWorstPerformanceQuestion(String question, String currency) {
        List<ProfitResponseDto> allProfits = profitClientService.getAllProfits();
        
        if (allProfits.isEmpty()) {
            return "No profit data available.";
        }
        
        Optional<ProfitResponseDto> worstMonth = allProfits.stream()
            .min((p1, p2) -> p1.getProfit().compareTo(p2.getProfit()));
        
        if (worstMonth.isPresent()) {
            BigDecimal worstProfit = worstMonth.get().getProfit();
            
            if (!currency.equalsIgnoreCase("USD")) {
                Optional<ConversionResponse> conversion = currencyClientService.convertCurrency(
                    worstProfit, "USD", currency);
                if (conversion.isPresent()) {
                    worstProfit = conversion.get().getConvertedAmount();
                }
            }
            
            return String.format("The worst performing month was %s with a profit of %s %s (%s).",
                worstMonth.get().getMonth().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                currency, worstProfit, worstMonth.get().getDescription());
        }
        
        return "Could not determine the worst performing month.";
    }
    
    private String extractMonth(String question) {
        // Try to extract month/year patterns
        Pattern monthYearPattern = Pattern.compile("(\\d{4})-(\\d{1,2})");
        Matcher matcher = monthYearPattern.matcher(question);
        if (matcher.find()) {
            return matcher.group();
        }
        
        // Try to extract month names with years
        String[] months = {"january", "february", "march", "april", "may", "june",
                          "july", "august", "september", "october", "november", "december"};
        
        for (int i = 0; i < months.length; i++) {
            if (question.toLowerCase().contains(months[i])) {
                Integer year = extractYear(question);
                if (year != null) {
                    return String.format("%d-%02d", year, i + 1);
                }
            }
        }
        
        return null;
    }
    
    private Integer extractYear(String question) {
        Pattern yearPattern = Pattern.compile("(20\\d{2})");
        Matcher matcher = yearPattern.matcher(question);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return null;
    }
    
    private String formatMonth(String monthStr) {
        try {
            YearMonth yearMonth = YearMonth.parse(monthStr);
            return yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        } catch (Exception e) {
            return monthStr;
        }
    }
}
package com.profitsystem.profit.service;

import com.profitsystem.profit.dto.ProfitResponseDto;
import com.profitsystem.profit.entity.MonthlyProfit;
import com.profitsystem.profit.repository.MonthlyProfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfitService {
    
    @Autowired
    private MonthlyProfitRepository repository;
    
    public Optional<ProfitResponseDto> getProfitByMonth(String monthStr) {
        try {
            YearMonth month = parseMonth(monthStr);
            Optional<MonthlyProfit> profit = repository.findByMonth(month);
            return profit.map(this::convertToDto);
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }
    
    public List<ProfitResponseDto> getAllProfits() {
        return repository.findAllOrderByMonthDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<ProfitResponseDto> getProfitsByYear(int year) {
        return repository.findByYear(year).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public BigDecimal getTotalProfitForYear(int year) {
        BigDecimal total = repository.getTotalProfitForYear(year);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    private YearMonth parseMonth(String monthStr) {
        // Try different formats
        try {
            // Try YYYY-MM format
            return YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("yyyy-MM"));
        } catch (DateTimeParseException e1) {
            try {
                // Try YYYY/MM format
                return YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("yyyy/MM"));
            } catch (DateTimeParseException e2) {
                try {
                    // Try MM-YYYY format
                    return YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("MM-yyyy"));
                } catch (DateTimeParseException e3) {
                    // Try MM/YYYY format
                    return YearMonth.parse(monthStr, DateTimeFormatter.ofPattern("MM/yyyy"));
                }
            }
        }
    }
    
    private ProfitResponseDto convertToDto(MonthlyProfit profit) {
        return new ProfitResponseDto(profit.getMonth(), profit.getProfit(), profit.getDescription());
    }
}
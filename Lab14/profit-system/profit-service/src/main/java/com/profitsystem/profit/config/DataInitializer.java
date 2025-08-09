package com.profitsystem.profit.config;

import com.profitsystem.profit.entity.MonthlyProfit;
import com.profitsystem.profit.repository.MonthlyProfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.YearMonth;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private MonthlyProfitRepository repository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize sample profit data for 2023 and 2024
        initializeProfitData();
    }
    
    private void initializeProfitData() {
        // 2023 data
        repository.save(new MonthlyProfit(YearMonth.of(2023, 1), new BigDecimal("85000.00"), "Strong Q1 performance"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 2), new BigDecimal("92000.00"), "Valentine's campaign success"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 3), new BigDecimal("78000.00"), "Market adjustment"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 4), new BigDecimal("95000.00"), "Spring product launch"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 5), new BigDecimal("103000.00"), "Best month of Q2"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 6), new BigDecimal("89000.00"), "Summer preparation"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 7), new BigDecimal("110000.00"), "Summer peak sales"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 8), new BigDecimal("98000.00"), "Back to school promotion"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 9), new BigDecimal("87000.00"), "Market stabilization"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 10), new BigDecimal("125000.00"), "Halloween and fall sales"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 11), new BigDecimal("140000.00"), "Black Friday success"));
        repository.save(new MonthlyProfit(YearMonth.of(2023, 12), new BigDecimal("165000.00"), "Holiday season peak"));
        
        // 2024 data
        repository.save(new MonthlyProfit(YearMonth.of(2024, 1), new BigDecimal("95000.00"), "New year momentum"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 2), new BigDecimal("88000.00"), "Post-holiday adjustment"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 3), new BigDecimal("105000.00"), "Q1 growth"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 4), new BigDecimal("112000.00"), "Spring expansion"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 5), new BigDecimal("118000.00"), "Strong performance"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 6), new BigDecimal("108000.00"), "Mid-year results"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 7), new BigDecimal("125000.00"), "Summer peak"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 8), new BigDecimal("115000.00"), "Sustained growth"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 9), new BigDecimal("102000.00"), "Market consolidation"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 10), new BigDecimal("135000.00"), "Fall campaign success"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 11), new BigDecimal("150000.00"), "Pre-holiday surge"));
        repository.save(new MonthlyProfit(YearMonth.of(2024, 12), new BigDecimal("175000.00"), "Record holiday sales"));
    }
}
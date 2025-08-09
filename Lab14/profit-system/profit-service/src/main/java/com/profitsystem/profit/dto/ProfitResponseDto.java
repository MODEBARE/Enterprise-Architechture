package com.profitsystem.profit.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public class ProfitResponseDto {
    private YearMonth month;
    private BigDecimal profit;
    private String description;
    
    public ProfitResponseDto() {}
    
    public ProfitResponseDto(YearMonth month, BigDecimal profit, String description) {
        this.month = month;
        this.profit = profit;
        this.description = description;
    }
    
    public YearMonth getMonth() {
        return month;
    }
    
    public void setMonth(YearMonth month) {
        this.month = month;
    }
    
    public BigDecimal getProfit() {
        return profit;
    }
    
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
package com.profitsystem.profit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.YearMonth;

@Entity
@Table(name = "monthly_profit")
public class MonthlyProfit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private YearMonth month;
    
    @NotNull
    @Column(precision = 15, scale = 2)
    private BigDecimal profit;
    
    @Column(length = 500)
    private String description;
    
    // Default constructor
    public MonthlyProfit() {}
    
    // Constructor
    public MonthlyProfit(YearMonth month, BigDecimal profit, String description) {
        this.month = month;
        this.profit = profit;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    @Override
    public String toString() {
        return "MonthlyProfit{" +
                "id=" + id +
                ", month=" + month +
                ", profit=" + profit +
                ", description='" + description + '\'' +
                '}';
    }
}
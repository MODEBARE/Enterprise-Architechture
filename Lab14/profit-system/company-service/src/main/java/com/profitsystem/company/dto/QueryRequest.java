package com.profitsystem.company.dto;

import jakarta.validation.constraints.NotBlank;

public class QueryRequest {
    
    @NotBlank
    private String question;
    
    private String currency = "USD"; // Default currency
    
    public QueryRequest() {}
    
    public QueryRequest(String question, String currency) {
        this.question = question;
        this.currency = currency;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
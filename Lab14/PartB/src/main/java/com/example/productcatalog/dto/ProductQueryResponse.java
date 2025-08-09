package com.example.productcatalog.dto;

import com.example.productcatalog.model.Product;
import java.time.LocalDateTime;
import java.util.List;

public class ProductQueryResponse {
    
    private String query;
    private String answer;
    private LocalDateTime timestamp;
    private String sessionId;
    private List<Product> relevantProducts;
    private List<String> suggestedCategories;
    private List<String> suggestedBrands;
    
    public ProductQueryResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ProductQueryResponse(String query, String answer, String sessionId) {
        this();
        this.query = query;
        this.answer = answer;
        this.sessionId = sessionId;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public List<Product> getRelevantProducts() {
        return relevantProducts;
    }
    
    public void setRelevantProducts(List<Product> relevantProducts) {
        this.relevantProducts = relevantProducts;
    }
    
    public List<String> getSuggestedCategories() {
        return suggestedCategories;
    }
    
    public void setSuggestedCategories(List<String> suggestedCategories) {
        this.suggestedCategories = suggestedCategories;
    }
    
    public List<String> getSuggestedBrands() {
        return suggestedBrands;
    }
    
    public void setSuggestedBrands(List<String> suggestedBrands) {
        this.suggestedBrands = suggestedBrands;
    }
}
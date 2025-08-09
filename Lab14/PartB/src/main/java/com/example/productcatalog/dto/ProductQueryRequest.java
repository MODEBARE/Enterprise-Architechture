package com.example.productcatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductQueryRequest {
    
    @NotBlank(message = "Query cannot be empty")
    @Size(max = 1000, message = "Query cannot exceed 1000 characters")
    private String query;
    
    private String sessionId;
    
    private String category;
    
    private String brand;
    
    public ProductQueryRequest() {}
    
    public ProductQueryRequest(String query, String sessionId) {
        this.query = query;
        this.sessionId = sessionId;
    }
    
    public String getQuery() {
        return query;
    }
    
    public void setQuery(String query) {
        this.query = query;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
}
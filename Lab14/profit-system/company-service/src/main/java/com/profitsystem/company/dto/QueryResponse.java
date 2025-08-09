package com.profitsystem.company.dto;

public class QueryResponse {
    
    private String question;
    private String answer;
    private String currency;
    private String timestamp;
    
    public QueryResponse() {}
    
    public QueryResponse(String question, String answer, String currency, String timestamp) {
        this.question = question;
        this.answer = answer;
        this.currency = currency;
        this.timestamp = timestamp;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
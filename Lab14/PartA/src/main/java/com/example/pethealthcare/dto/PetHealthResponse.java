package com.example.pethealthcare.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PetHealthResponse {
    
    private Long id;
    private String question;
    private String answer;
    private LocalDateTime timestamp;
    private String sessionId;
    private List<PetHealthResponse> conversationHistory;
    
    public PetHealthResponse() {}
    
    public PetHealthResponse(Long id, String question, String answer, LocalDateTime timestamp, String sessionId) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.timestamp = timestamp;
        this.sessionId = sessionId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public List<PetHealthResponse> getConversationHistory() {
        return conversationHistory;
    }
    
    public void setConversationHistory(List<PetHealthResponse> conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
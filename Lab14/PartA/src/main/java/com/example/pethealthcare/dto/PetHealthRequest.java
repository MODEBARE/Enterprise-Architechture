package com.example.pethealthcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PetHealthRequest {
    
    @NotBlank(message = "Question cannot be empty")
    @Size(max = 2000, message = "Question cannot exceed 2000 characters")
    private String question;
    
    private String sessionId;
    
    public PetHealthRequest() {}
    
    public PetHealthRequest(String question, String sessionId) {
        this.question = question;
        this.sessionId = sessionId;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
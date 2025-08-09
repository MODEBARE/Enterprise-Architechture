package com.example.pethealthcare.controller;

import com.example.pethealthcare.dto.PetHealthRequest;
import com.example.pethealthcare.dto.PetHealthResponse;
import com.example.pethealthcare.service.PetHealthcareService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pet-health")
@CrossOrigin(origins = "*")
public class PetHealthcareController {
    
    private static final Logger logger = LoggerFactory.getLogger(PetHealthcareController.class);
    
    private final PetHealthcareService petHealthcareService;
    
    public PetHealthcareController(PetHealthcareService petHealthcareService) {
        this.petHealthcareService = petHealthcareService;
    }
    
    @PostMapping("/ask")
    public ResponseEntity<Map<String, Object>> askQuestion(@Valid @RequestBody PetHealthRequest request) {
        logger.info("POST /api/pet-health/ask - Question received: {}", request.getQuestion());
        
        try {
            PetHealthResponse response = petHealthcareService.askQuestion(request);
            
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", true);
            jsonResponse.put("data", response);
            jsonResponse.put("message", "Question processed successfully");
            
            logger.info("Question processed successfully with ID: {}", response.getId());
            
            return ResponseEntity.ok(jsonResponse);
            
        } catch (Exception e) {
            logger.error("Error processing question: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to process question");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<Map<String, Object>> getConversationHistory(@PathVariable String sessionId) {
        logger.info("GET /api/pet-health/history/{} - Retrieving conversation history", sessionId);
        
        try {
            List<PetHealthResponse> history = petHealthcareService.getConversationHistory(sessionId);
            
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", true);
            jsonResponse.put("data", history);
            jsonResponse.put("sessionId", sessionId);
            jsonResponse.put("totalQuestions", history.size());
            jsonResponse.put("message", "Conversation history retrieved successfully");
            
            return ResponseEntity.ok(jsonResponse);
            
        } catch (Exception e) {
            logger.error("Error retrieving conversation history: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to retrieve conversation history");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllQuestions() {
        logger.info("GET /api/pet-health/all - Retrieving all questions and answers");
        
        try {
            List<PetHealthResponse> allQuestions = petHealthcareService.getAllQuestions();
            
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", true);
            jsonResponse.put("data", allQuestions);
            jsonResponse.put("totalQuestions", allQuestions.size());
            jsonResponse.put("message", "All questions retrieved successfully");
            
            return ResponseEntity.ok(jsonResponse);
            
        } catch (Exception e) {
            logger.error("Error retrieving all questions: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Failed to retrieve questions");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Pet Healthcare AI");
        response.put("message", "Service is running successfully");
        
        return ResponseEntity.ok(response);
    }
}
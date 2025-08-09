package com.profitsystem.company.controller;

import com.profitsystem.company.dto.QueryRequest;
import com.profitsystem.company.dto.QueryResponse;
import com.profitsystem.company.service.ProfitTool;
import com.profitsystem.company.service.CurrencyTool;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "*")
public class CompanyController {
    
    @Autowired
    private ChatClient chatClient;
    
    @Autowired
    private ProfitTool profitTool;
    
    @Autowired
    private CurrencyTool currencyTool;
    
    @PostMapping("/query")
    public ResponseEntity<QueryResponse> askQuestion(@Valid @RequestBody QueryRequest request) {
        try {
            String answer = chatClient.prompt()
                .tools(profitTool)
                .tools(currencyTool)
                .user(u -> u.text("Please help me with company profit information. " +
                    "Currency preference: " + request.getCurrency() + ". " +
                    "Question: " + request.getQuestion())
                    .param("currency", request.getCurrency()))
                .call()
                .content();
            
            QueryResponse response = new QueryResponse(
                request.getQuestion(),
                answer,
                request.getCurrency(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            QueryResponse errorResponse = new QueryResponse(
                request.getQuestion(),
                "Sorry, I encountered an error processing your question: " + e.getMessage(),
                request.getCurrency(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    @GetMapping("/query")
    public ResponseEntity<QueryResponse> askQuestionGet(
            @RequestParam String question,
            @RequestParam(defaultValue = "USD") String currency) {
        
        try {
            String answer = chatClient.prompt()
                .tools(profitTool)
                .tools(currencyTool)
                .user(u -> u.text("Please help me with company profit information. " +
                    "Currency preference: " + currency + ". " +
                    "Question: " + question)
                    .param("currency", currency))
                .call()
                .content();
            
            QueryResponse response = new QueryResponse(
                question,
                answer,
                currency,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            QueryResponse errorResponse = new QueryResponse(
                question,
                "Sorry, I encountered an error processing your question: " + e.getMessage(),
                currency,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Company Service is running");
    }
    
    @GetMapping("/examples")
    public ResponseEntity<String[]> getExampleQuestions() {
        String[] examples = {
            "What was the profit in January 2024?",
            "Show me the total profit for 2023",
            "What was our best performing month?",
            "Which month had the lowest profit?",
            "What is the overall profit across all months?",
            "Convert the profit for December 2024 to EUR",
            "Show me yearly profit in GBP"
        };
        return ResponseEntity.ok(examples);
    }
}
package com.example.pethealthcare.service;

import com.example.pethealthcare.dto.PetHealthRequest;
import com.example.pethealthcare.dto.PetHealthResponse;
import com.example.pethealthcare.model.QuestionAnswer;
import com.example.pethealthcare.repository.QuestionAnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetHealthcareService {
    
    private static final Logger logger = LoggerFactory.getLogger(PetHealthcareService.class);
    
    private final ChatClient chatClient;
    private final QuestionAnswerRepository repository;
    
    private static final String SYSTEM_PROMPT = """
        You are a professional veterinary assistant specializing in pet healthcare.
        Your role is to provide helpful, accurate, and compassionate advice about pet health concerns.
        
        Guidelines:
        - Always remind users that your advice should not replace professional veterinary care
        - Provide helpful information while encouraging users to consult with a veterinarian for serious concerns
        - Be empathetic and understanding about pet owners' concerns
        - If a situation seems urgent or serious, strongly recommend immediate veterinary attention
        - Keep responses informative but concise
        - Focus on common pet health topics like nutrition, exercise, preventive care, and minor health issues
        
        Remember: You are providing educational information, not diagnosing or treating specific medical conditions.
        """;
    
    public PetHealthcareService(ChatClient.Builder chatClientBuilder, QuestionAnswerRepository repository) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
    }
    
    public PetHealthResponse askQuestion(PetHealthRequest request) {
        logger.info("Received question: {} for session: {}", request.getQuestion(), request.getSessionId());
        
        // Generate session ID if not provided
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        
        try {
            // Get conversation history for context
            List<QuestionAnswer> history = repository.findBySessionIdOrderByCreatedAtAsc(sessionId);
            
            // Build the conversation context
            List<Message> messages = new ArrayList<>();
            messages.add(new SystemMessage(SYSTEM_PROMPT));
            
            // Add conversation history for context
            for (QuestionAnswer qa : history) {
                messages.add(new UserMessage(qa.getQuestion()));
                messages.add(new org.springframework.ai.chat.messages.AssistantMessage(qa.getAnswer()));
            }
            
            // Add current question
            messages.add(new UserMessage(request.getQuestion()));
            
            // Create prompt with conversation context
            Prompt prompt = new Prompt(messages);
            
            // Get AI response
            String aiResponse = chatClient.prompt(prompt).call().content();
            
            logger.info("AI response generated for session: {}", sessionId);
            
            // Save question and answer to database
            QuestionAnswer qa = new QuestionAnswer(request.getQuestion(), aiResponse, sessionId);
            qa = repository.save(qa);
            
            logger.info("Question and answer saved with ID: {}", qa.getId());
            
            // Create response with conversation history
            PetHealthResponse response = new PetHealthResponse(
                qa.getId(),
                qa.getQuestion(),
                qa.getAnswer(),
                qa.getCreatedAt(),
                qa.getSessionId()
            );
            
            // Include recent conversation history in response
            List<PetHealthResponse> conversationHistory = history.stream()
                .map(h -> new PetHealthResponse(h.getId(), h.getQuestion(), h.getAnswer(), h.getCreatedAt(), h.getSessionId()))
                .collect(Collectors.toList());
            
            response.setConversationHistory(conversationHistory);
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error processing question for session {}: {}", sessionId, e.getMessage(), e);
            throw new RuntimeException("Failed to process pet healthcare question", e);
        }
    }
    
    public List<PetHealthResponse> getConversationHistory(String sessionId) {
        logger.info("Retrieving conversation history for session: {}", sessionId);
        
        List<QuestionAnswer> history = repository.findBySessionIdOrderByCreatedAtAsc(sessionId);
        
        return history.stream()
            .map(qa -> new PetHealthResponse(qa.getId(), qa.getQuestion(), qa.getAnswer(), qa.getCreatedAt(), qa.getSessionId()))
            .collect(Collectors.toList());
    }
    
    public List<PetHealthResponse> getAllQuestions() {
        logger.info("Retrieving all questions and answers");
        
        List<QuestionAnswer> allQAs = repository.findAllOrderByCreatedAtDesc();
        
        return allQAs.stream()
            .map(qa -> new PetHealthResponse(qa.getId(), qa.getQuestion(), qa.getAnswer(), qa.getCreatedAt(), qa.getSessionId()))
            .collect(Collectors.toList());
    }
}
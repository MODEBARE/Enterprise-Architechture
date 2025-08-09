package com.profitsystem.company.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {
    
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
            .defaultSystem("You are a helpful AI assistant for a company's profit analysis system. " +
                "You have access to tools that can retrieve profit data and convert currencies. " +
                "Use these tools to provide accurate, detailed responses about company profits. " +
                "When users ask about profits, always specify amounts in the requested currency. " +
                "If no currency is specified, use USD as default. " +
                "Be professional and provide clear, informative responses.")
            .build();
    }
}
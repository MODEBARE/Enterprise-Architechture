package com.example.productcatalog.controller;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    @Autowired
    private ChatClient.Builder chatClientBuilder;
    
    @Autowired
    private ProductRepository productRepository;
    
    private ChatClient chatClient;
    
    @PostMapping("/chat")
    public Map<String, Object> chat(@RequestParam(value = "message") String message) throws IOException {
        
        // Get all products from database
        List<Product> products = productRepository.findAll();
        
        // Build product content string (this is the "prompt stuffing")
        String productContent = buildProductContent(products);
        
        // Create system prompt with product data stuffed in
        String systemPrompt = """
            You are a helpful product assistant. Answer questions about products based on the following product catalog data:
            
            """ + productContent + """
            
            Please provide helpful and accurate answers based only on the products listed above.
            If a product is not in the catalog, let the user know it's not available.
            """;
        
        // Initialize chat client if not already done
        if (chatClient == null) {
            chatClient = chatClientBuilder.build();
        }
        
        // Get AI response with stuffed product data
        String response = chatClient.prompt()
            .system(systemPrompt)
            .user(message)
            .call()
            .content();
        
        logger.info("User question: {} | AI Response generated", message);
        
        // Return response in JSON format
        Map<String, Object> result = new HashMap<>();
        result.put("question", message);
        result.put("answer", response);
        result.put("totalProducts", products.size());
        
        return result;
    }
    
    @GetMapping("/all")
    public Map<String, Object> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        Map<String, Object> result = new HashMap<>();
        result.put("products", products);
        result.put("total", products.size());
        
        return result;
    }
    
    private String buildProductContent(List<Product> products) {
        StringBuilder content = new StringBuilder();
        content.append("PRODUCT CATALOG:\n\n");
        
        for (Product product : products) {
            content.append("Product: ").append(product.getName()).append("\n");
            content.append("Brand: ").append(product.getBrand()).append("\n");
            content.append("Category: ").append(product.getCategory()).append("\n");
            content.append("Price: $").append(product.getPrice()).append("\n");
            content.append("Stock: ").append(product.getStockQuantity()).append(" units\n");
            if (product.getDescription() != null) {
                content.append("Description: ").append(product.getDescription()).append("\n");
            }
            content.append("Available: ").append(product.getAvailable() ? "Yes" : "No").append("\n");
            content.append("---\n");
        }
        
        return content.toString();
    }
}
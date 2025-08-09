package com.example.productcatalog.service;

import com.example.productcatalog.dto.ProductQueryRequest;
import com.example.productcatalog.dto.ProductQueryResponse;
import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    private final ChatClient chatClient;
    private final ProductRepository productRepository;
    
    public ProductService(ChatClient.Builder chatClientBuilder, ProductRepository productRepository) {
        this.chatClient = chatClientBuilder.build();
        this.productRepository = productRepository;
    }
    
    public ProductQueryResponse queryProducts(ProductQueryRequest request) {
        logger.info("Received product query: {} for session: {}", request.getQuery(), request.getSessionId());
        
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        
        try {
            // Get product catalog data
            List<Product> allProducts = productRepository.findByAvailableTrue();
            List<String> categories = productRepository.findAllCategories();
            List<String> brands = productRepository.findAllBrands();
            
            // Find relevant products based on query
            List<Product> relevantProducts = findRelevantProducts(request.getQuery(), request.getCategory(), request.getBrand());
            
            // Create context for AI
            String productContext = buildProductContext(allProducts, categories, brands, relevantProducts);
            
            // Create system prompt with product stuffing
            String systemPrompt = createSystemPrompt(productContext);
            
            // Create user query
            String userQuery = buildUserQuery(request.getQuery(), request.getCategory(), request.getBrand());
            
            // Get AI response
            Prompt prompt = new Prompt(List.of(
                new SystemMessage(systemPrompt),
                new UserMessage(userQuery)
            ));
            
            String aiResponse = chatClient.prompt(prompt).call().content();
            
            logger.info("AI response generated for session: {}", sessionId);
            
            // Create response
            ProductQueryResponse response = new ProductQueryResponse(request.getQuery(), aiResponse, sessionId);
            response.setRelevantProducts(relevantProducts.stream().limit(10).collect(Collectors.toList()));
            response.setSuggestedCategories(categories);
            response.setSuggestedBrands(brands);
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error processing product query for session {}: {}", sessionId, e.getMessage(), e);
            throw new RuntimeException("Failed to process product query", e);
        }
    }
    
    private List<Product> findRelevantProducts(String query, String category, String brand) {
        List<Product> products;
        
        // Apply category filter if specified
        if (category != null && !category.isEmpty()) {
            products = productRepository.findAvailableByCategory(category);
        }
        // Apply brand filter if specified
        else if (brand != null && !brand.isEmpty()) {
            products = productRepository.findAvailableByBrand(brand);
        }
        // Search by keyword in product name and description
        else {
            products = productRepository.findByKeyword(query.toLowerCase());
            
            // If no keyword matches, return all available products
            if (products.isEmpty()) {
                products = productRepository.findByAvailableTrue();
            }
        }
        
        return products.stream().limit(20).collect(Collectors.toList());
    }
    
    private String buildProductContext(List<Product> allProducts, List<String> categories, List<String> brands, List<Product> relevantProducts) {
        StringBuilder context = new StringBuilder();
        
        context.append("PRODUCT CATALOG INFORMATION:\n\n");
        
        context.append("Available Categories: ").append(String.join(", ", categories)).append("\n");
        context.append("Available Brands: ").append(String.join(", ", brands)).append("\n");
        context.append("Total Products in Catalog: ").append(allProducts.size()).append("\n\n");
        
        if (!relevantProducts.isEmpty()) {
            context.append("RELEVANT PRODUCTS FOR THIS QUERY:\n");
            for (Product product : relevantProducts.stream().limit(10).collect(Collectors.toList())) {
                context.append("- ").append(product.getName())
                    .append(" (").append(product.getBrand()).append(")")
                    .append(" - $").append(product.getPrice())
                    .append(" - Category: ").append(product.getCategory())
                    .append(" - Stock: ").append(product.getStockQuantity())
                    .append(" - Description: ").append(product.getDescription() != null ? product.getDescription().substring(0, Math.min(100, product.getDescription().length())) + "..." : "No description")
                    .append("\n");
            }
        }
        
        context.append("\nSAMPLE PRODUCTS FROM EACH CATEGORY:\n");
        for (String category : categories.stream().limit(5).collect(Collectors.toList())) {
            List<Product> categoryProducts = allProducts.stream()
                .filter(p -> category.equals(p.getCategory()))
                .limit(3)
                .collect(Collectors.toList());
            
            context.append("Category: ").append(category).append("\n");
            for (Product product : categoryProducts) {
                context.append("  - ").append(product.getName())
                    .append(" ($").append(product.getPrice()).append(")")
                    .append(" by ").append(product.getBrand()).append("\n");
            }
        }
        
        return context.toString();
    }
    
    private String createSystemPrompt(String productContext) {
        return """
            You are a helpful product catalog assistant for an e-commerce store.
            Your role is to help customers find products, compare options, and make informed purchasing decisions.
            
            Guidelines:
            - Use the provided product catalog information to answer questions accurately
            - Recommend specific products when appropriate
            - Provide helpful comparisons between products
            - Mention prices, availability, and key features
            - If a product is out of stock, suggest alternatives
            - Be enthusiastic but honest about product capabilities
            - Help users navigate categories and brands
            - Provide clear, actionable advice
            
            IMPORTANT: Base your responses ONLY on the product information provided below.
            Do not invent products, prices, or features that are not in the catalog.
            
            """ + productContext;
    }
    
    private String buildUserQuery(String query, String category, String brand) {
        StringBuilder userQuery = new StringBuilder(query);
        
        if (category != null && !category.isEmpty()) {
            userQuery.append(" (Focus on category: ").append(category).append(")");
        }
        
        if (brand != null && !brand.isEmpty()) {
            userQuery.append(" (Focus on brand: ").append(brand).append(")");
        }
        
        return userQuery.toString();
    }
    
    // Additional service methods
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAvailableByCategory(category);
    }
    
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findAvailableByBrand(brand);
    }
    
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }
    
    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByKeyword(keyword);
    }
}
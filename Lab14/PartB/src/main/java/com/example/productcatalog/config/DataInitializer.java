package com.example.productcatalog.config;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }
    
    private void initializeProducts() {
        // Electronics
        productRepository.save(new Product(
            "iPhone 15 Pro",
            "Latest iPhone with A17 Pro chip, titanium design, and advanced camera system",
            new BigDecimal("999.99"),
            "Electronics",
            "Apple",
            50,
            "IPH15PRO"
        ));
        
        productRepository.save(new Product(
            "Samsung Galaxy S24",
            "Flagship Android phone with AI features and excellent camera",
            new BigDecimal("799.99"),
            "Electronics",
            "Samsung",
            30,
            "SGS24"
        ));
        
        productRepository.save(new Product(
            "MacBook Air M3",
            "13-inch laptop with M3 chip, perfect for everyday computing",
            new BigDecimal("1099.99"),
            "Electronics",
            "Apple",
            25,
            "MBA13M3"
        ));
        
        productRepository.save(new Product(
            "Dell XPS 13",
            "Premium ultrabook with Intel Core i7 and stunning display",
            new BigDecimal("1299.99"),
            "Electronics",
            "Dell",
            20,
            "DXPS13"
        ));
        
        // Clothing
        productRepository.save(new Product(
            "Classic Blue Jeans",
            "Comfortable straight-fit denim jeans in classic blue wash",
            new BigDecimal("79.99"),
            "Clothing",
            "Levi's",
            100,
            "LEV501"
        ));
        
        productRepository.save(new Product(
            "Cotton T-Shirt",
            "100% organic cotton t-shirt available in multiple colors",
            new BigDecimal("24.99"),
            "Clothing",
            "H&M",
            200,
            "HM001"
        ));
        
        productRepository.save(new Product(
            "Running Sneakers",
            "Lightweight running shoes with excellent cushioning",
            new BigDecimal("129.99"),
            "Clothing",
            "Nike",
            75,
            "NIKERUN"
        ));
        
        // Home & Garden
        productRepository.save(new Product(
            "Coffee Maker",
            "12-cup programmable coffee maker with auto-shutoff",
            new BigDecimal("89.99"),
            "Home & Garden",
            "Cuisinart",
            40,
            "CUISCM"
        ));
        
        productRepository.save(new Product(
            "Air Purifier",
            "HEPA air purifier suitable for large rooms up to 500 sq ft",
            new BigDecimal("199.99"),
            "Home & Garden",
            "Honeywell",
            30,
            "HONAP"
        ));
        
        productRepository.save(new Product(
            "Garden Tool Set",
            "5-piece stainless steel garden tool set with carrying case",
            new BigDecimal("49.99"),
            "Home & Garden",
            "Fiskars",
            60,
            "FISKGT"
        ));
        
        // Books
        productRepository.save(new Product(
            "The Great Gatsby",
            "Classic American novel by F. Scott Fitzgerald",
            new BigDecimal("12.99"),
            "Books",
            "Scribner",
            150,
            "TGG001"
        ));
        
        productRepository.save(new Product(
            "Programming with Spring Boot",
            "Complete guide to building applications with Spring Boot",
            new BigDecimal("45.99"),
            "Books",
            "O'Reilly",
            80,
            "SB2024"
        ));
        
        // Sports
        productRepository.save(new Product(
            "Yoga Mat",
            "Non-slip premium yoga mat with extra cushioning",
            new BigDecimal("39.99"),
            "Sports",
            "Manduka",
            90,
            "MANDYOGA"
        ));
        
        productRepository.save(new Product(
            "Basketball",
            "Official size basketball suitable for indoor and outdoor play",
            new BigDecimal("29.99"),
            "Sports",
            "Spalding",
            120,
            "SPALBB"
        ));
        
        // Food & Beverages
        productRepository.save(new Product(
            "Organic Green Tea",
            "Premium organic green tea leaves, 100 tea bags",
            new BigDecimal("18.99"),
            "Food & Beverages",
            "Twinings",
            200,
            "TWINGT"
        ));
        
        productRepository.save(new Product(
            "Dark Chocolate Bar",
            "70% cocoa dark chocolate bar, fair trade certified",
            new BigDecimal("4.99"),
            "Food & Beverages",
            "Lindt",
            300,
            "LINDTDC"
        ));
        
        System.out.println("Initialized " + productRepository.count() + " products in the database");
    }
}
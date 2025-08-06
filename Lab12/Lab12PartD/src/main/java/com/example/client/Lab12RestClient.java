package com.example.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Lab12RestClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080";
    
    public Lab12RestClient() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Test all endpoints with different credentials
     */
    public void testAllEndpoints() {
        System.out.println("=".repeat(80));
        System.out.println("LAB 12 CLIENT - TESTING SERVER ENDPOINTS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Test public endpoint (no authentication required)
        testPublicEndpoint();
        
        // Test with Bob's credentials (EMPLOYEE, SALES)
        testWithBobCredentials();
        
        // Test with Mary's credentials (EMPLOYEE, FINANCE)  
        testWithMaryCredentials();
        
        // Test unauthorized access attempts
        testUnauthorizedAccess();
        
        System.out.println("=".repeat(80));
        System.out.println("CLIENT TESTING COMPLETED");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Test the public /shop endpoint (no authentication required)
     */
    private void testPublicEndpoint() {
        System.out.println("1. TESTING PUBLIC ENDPOINT");
        System.out.println("-".repeat(40));
        
        try {
            String response = restTemplate.getForObject(baseUrl + "/shop", String.class);
            System.out.println("✅ GET /shop (no auth): SUCCESS");
            System.out.println("   Response: " + response);
        } catch (Exception e) {
            System.out.println("❌ GET /shop (no auth): FAILED");
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Test endpoints with Bob's credentials (EMPLOYEE, SALES roles)
     */
    private void testWithBobCredentials() {
        System.out.println("2. TESTING WITH BOB'S CREDENTIALS (EMPLOYEE, SALES)");
        System.out.println("-".repeat(50));
        
        HttpHeaders headers = createAuthHeaders("bob", "password");
        
        // Test /shop endpoint
        testEndpoint("GET", "/shop", headers, "Bob", true);
        
        // Test /orders endpoint (should succeed - Bob has EMPLOYEE role)
        testEndpoint("GET", "/orders", headers, "Bob", true);
        
        // Test /payments endpoint (should fail - Bob doesn't have FINANCE role)
        testEndpoint("GET", "/payments", headers, "Bob", false);
        
        System.out.println();
    }
    
    /**
     * Test endpoints with Mary's credentials (EMPLOYEE, FINANCE roles)
     */
    private void testWithMaryCredentials() {
        System.out.println("3. TESTING WITH MARY'S CREDENTIALS (EMPLOYEE, FINANCE)");
        System.out.println("-".repeat(52));
        
        HttpHeaders headers = createAuthHeaders("mary", "password");
        
        // Test /shop endpoint
        testEndpoint("GET", "/shop", headers, "Mary", true);
        
        // Test /orders endpoint (should succeed - Mary has EMPLOYEE role)
        testEndpoint("GET", "/orders", headers, "Mary", true);
        
        // Test /payments endpoint (should succeed - Mary has FINANCE role)
        testEndpoint("GET", "/payments", headers, "Mary", true);
        
        System.out.println();
    }
    
    /**
     * Test unauthorized access attempts
     */
    private void testUnauthorizedAccess() {
        System.out.println("4. TESTING UNAUTHORIZED ACCESS");
        System.out.println("-".repeat(35));
        
        // Test with wrong credentials
        HttpHeaders wrongHeaders = createAuthHeaders("wrong", "credentials");
        testEndpoint("GET", "/orders", wrongHeaders, "Wrong User", false);
        
        // Test without credentials
        testEndpoint("GET", "/orders", new HttpHeaders(), "No Auth", false);
        
        System.out.println();
    }
    
    /**
     * Test a specific endpoint
     */
    private void testEndpoint(String method, String endpoint, HttpHeaders headers, String user, boolean expectSuccess) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                baseUrl + endpoint, 
                HttpMethod.valueOf(method), 
                entity, 
                String.class
            );
            
            if (expectSuccess) {
                System.out.println("✅ " + method + " " + endpoint + " (" + user + "): SUCCESS");
                System.out.println("   Status: " + response.getStatusCode());
                System.out.println("   Response: " + response.getBody());
            } else {
                System.out.println("⚠️  " + method + " " + endpoint + " (" + user + "): UNEXPECTED SUCCESS");
                System.out.println("   Status: " + response.getStatusCode());
                System.out.println("   Response: " + response.getBody());
            }
            
        } catch (HttpClientErrorException e) {
            if (!expectSuccess) {
                System.out.println("✅ " + method + " " + endpoint + " (" + user + "): CORRECTLY DENIED");
                System.out.println("   Status: " + e.getStatusCode());
                System.out.println("   Error: " + e.getStatusText());
            } else {
                System.out.println("❌ " + method + " " + endpoint + " (" + user + "): UNEXPECTED FAILURE");
                System.out.println("   Status: " + e.getStatusCode());
                System.out.println("   Error: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("❌ " + method + " " + endpoint + " (" + user + "): ERROR");
            System.out.println("   Error: " + e.getMessage());
        }
    }
    
    /**
     * Create HTTP headers with Basic Authentication
     */
    private HttpHeaders createAuthHeaders(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(
            credentials.getBytes(StandardCharsets.UTF_8)
        );
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
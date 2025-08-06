package com.example.jwtclient;

import com.example.jwtclient.dto.JwtAuthenticationResponse;
import com.example.jwtclient.dto.SignInRequest;
import com.example.jwtclient.dto.SignUpRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class JwtRestClient {
    
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080";
    
    public JwtRestClient() {
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * Test all endpoints with different user roles
     */
    public void testAllEndpoints() {
        System.out.println("=".repeat(80));
        System.out.println("LAB 12 PART C CLIENT - TESTING JWT SERVER ENDPOINTS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Test public endpoint (no authentication required)
        testPublicEndpoint();
        
        // Register test users
        String userToken = registerAndLoginUser("USER");
        String adminToken = registerAndLoginAdmin("ADMIN");
        
        // Test with USER credentials
        testWithUserToken(userToken);
        
        // Test with ADMIN credentials
        testWithAdminToken(adminToken);
        
        // Test unauthorized access attempts
        testUnauthorizedAccess();
        
        System.out.println("=".repeat(80));
        System.out.println("JWT CLIENT TESTING COMPLETED");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Test the public /api/all endpoint (no authentication required)
     */
    private void testPublicEndpoint() {
        System.out.println("1. TESTING PUBLIC ENDPOINT");
        System.out.println("-".repeat(40));
        
        try {
            String response = restTemplate.getForObject(baseUrl + "/api/all", String.class);
            System.out.println("✅ GET /api/all (no auth): SUCCESS");
            System.out.println("   Response: " + response);
        } catch (Exception e) {
            System.out.println("❌ GET /api/all (no auth): FAILED");
            System.out.println("   Error: " + e.getMessage());
        }
        System.out.println();
    }
    
    /**
     * Register and login a user, return JWT token
     */
    private String registerAndLoginUser(String role) {
        System.out.println("2. REGISTERING AND AUTHENTICATING " + role + " USER");
        System.out.println("-".repeat(50));
        
        try {
            // Register user
            SignUpRequest signUpRequest = new SignUpRequest(
                "Test", "User", "user@example.com", "password123"
            );
            
            JwtAuthenticationResponse signUpResponse = restTemplate.postForObject(
                baseUrl + "/auth/signup", signUpRequest, JwtAuthenticationResponse.class
            );
            
            System.out.println("✅ User registration: SUCCESS");
            System.out.println("   JWT Token received: " + (signUpResponse.getToken().substring(0, 20) + "..."));
            
            return signUpResponse.getToken();
            
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // User might already exist, try to login
                System.out.println("⚠️  User already exists, attempting login...");
                return loginUser("user@example.com", "password123");
            } else {
                System.out.println("❌ User registration failed: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ User registration error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Register and login an admin, return JWT token
     */
    private String registerAndLoginAdmin(String role) {
        System.out.println("3. REGISTERING AND AUTHENTICATING " + role + " USER");
        System.out.println("-".repeat(52));
        
        try {
            // Register admin
            SignUpRequest signUpRequest = new SignUpRequest(
                "Test", "Admin", "admin@example.com", "password123"
            );
            
            JwtAuthenticationResponse signUpResponse = restTemplate.postForObject(
                baseUrl + "/auth/signup", signUpRequest, JwtAuthenticationResponse.class
            );
            
            System.out.println("✅ Admin registration: SUCCESS");
            System.out.println("   JWT Token received: " + (signUpResponse.getToken().substring(0, 20) + "..."));
            
            return signUpResponse.getToken();
            
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                // Admin might already exist, try to login
                System.out.println("⚠️  Admin already exists, attempting login...");
                return loginUser("admin@example.com", "password123");
            } else {
                System.out.println("❌ Admin registration failed: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ Admin registration error: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Login user and return JWT token
     */
    private String loginUser(String email, String password) {
        try {
            SignInRequest signInRequest = new SignInRequest(email, password);
            JwtAuthenticationResponse response = restTemplate.postForObject(
                baseUrl + "/auth/signin", signInRequest, JwtAuthenticationResponse.class
            );
            
            System.out.println("✅ User login: SUCCESS");
            System.out.println("   JWT Token received: " + (response.getToken().substring(0, 20) + "..."));
            
            return response.getToken();
        } catch (Exception e) {
            System.out.println("❌ User login failed: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Test endpoints with USER token
     */
    private void testWithUserToken(String token) {
        if (token == null) {
            System.out.println("❌ Cannot test with USER token - token is null");
            return;
        }
        
        System.out.println("4. TESTING WITH USER JWT TOKEN");
        System.out.println("-".repeat(35));
        
        HttpHeaders headers = createJwtHeaders(token);
        
        // Test /api/all endpoint
        testEndpointWithJwt("GET", "/api/all", headers, "User", true);
        
        // Test /api/users endpoint (should succeed - user has USER role)
        testEndpointWithJwt("GET", "/api/users", headers, "User", true);
        
        // Test /api/admins endpoint (should fail - user doesn't have ADMIN role)
        testEndpointWithJwt("GET", "/api/admins", headers, "User", false);
        
        System.out.println();
    }
    
    /**
     * Test endpoints with ADMIN token
     */
    private void testWithAdminToken(String token) {
        if (token == null) {
            System.out.println("❌ Cannot test with ADMIN token - token is null");
            return;
        }
        
        System.out.println("5. TESTING WITH ADMIN JWT TOKEN");
        System.out.println("-".repeat(36));
        
        HttpHeaders headers = createJwtHeaders(token);
        
        // Test /api/all endpoint
        testEndpointWithJwt("GET", "/api/all", headers, "Admin", true);
        
        // Test /api/users endpoint (should succeed if admin has USER role too)
        testEndpointWithJwt("GET", "/api/users", headers, "Admin", true);
        
        // Test /api/admins endpoint (should succeed - admin has ADMIN role)
        testEndpointWithJwt("GET", "/api/admins", headers, "Admin", true);
        
        System.out.println();
    }
    
    /**
     * Test unauthorized access attempts
     */
    private void testUnauthorizedAccess() {
        System.out.println("6. TESTING UNAUTHORIZED ACCESS");
        System.out.println("-".repeat(35));
        
        // Test with invalid token
        HttpHeaders invalidHeaders = createJwtHeaders("invalid.jwt.token");
        testEndpointWithJwt("GET", "/api/users", invalidHeaders, "Invalid Token", false);
        
        // Test without token
        testEndpointWithJwt("GET", "/api/users", new HttpHeaders(), "No Token", false);
        
        System.out.println();
    }
    
    /**
     * Test a specific endpoint with JWT token
     */
    private void testEndpointWithJwt(String method, String endpoint, HttpHeaders headers, String user, boolean expectSuccess) {
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
     * Create HTTP headers with JWT Bearer token
     */
    private HttpHeaders createJwtHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
package com.profitsystem.company.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingClientHttpRequestInterceptor.class);
    
    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        
        logRequest(request, body);
        
        ClientHttpResponse response = execution.execute(request, body);
        
        // Wrap response to log it
        return new LoggingClientHttpResponse(response);
    }
    
    private void logRequest(HttpRequest request, byte[] body) {
        log.info("=== HTTP REQUEST ===");
        log.info("Method: {}", request.getMethod());
        log.info("URI: {}", request.getURI());
        log.info("Headers: {}", request.getHeaders());
        
        if (body.length > 0) {
            String bodyStr = new String(body, StandardCharsets.UTF_8);
            log.info("Request Body: {}", bodyStr);
        }
        log.info("===================");
    }
    
    private static class LoggingClientHttpResponse implements ClientHttpResponse {
        
        private final ClientHttpResponse response;
        private byte[] body;
        
        public LoggingClientHttpResponse(ClientHttpResponse response) {
            this.response = response;
        }
        
        @Override
        public org.springframework.http.HttpStatusCode getStatusCode() throws IOException {
            org.springframework.http.HttpStatusCode statusCode = response.getStatusCode();
            logResponse();
            return statusCode;
        }
        
        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }
        
        @Override
        public void close() {
            response.close();
        }
        
        @Override
        public java.io.InputStream getBody() throws IOException {
            if (body == null) {
                body = response.getBody().readAllBytes();
            }
            return new java.io.ByteArrayInputStream(body);
        }
        
        @Override
        public org.springframework.http.HttpHeaders getHeaders() {
            return response.getHeaders();
        }
        
        private void logResponse() throws IOException {
            log.info("=== HTTP RESPONSE ===");
            log.info("Status: {}", getStatusCode());
            log.info("Headers: {}", getHeaders());
            
            // Read body for logging
            if (body == null) {
                body = response.getBody().readAllBytes();
            }
            
            if (body.length > 0) {
                String bodyStr = new String(body, StandardCharsets.UTF_8);
                log.info("Response Body: {}", bodyStr);
            }
            log.info("====================");
        }
    }
}
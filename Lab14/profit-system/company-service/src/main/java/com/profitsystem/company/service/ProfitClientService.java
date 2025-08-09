package com.profitsystem.company.service;

import com.profitsystem.company.dto.ProfitResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProfitClientService {
    
    private static final Logger log = LoggerFactory.getLogger(ProfitClientService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${profit.service.url:http://localhost:8081}")
    private String profitServiceUrl;
    
    public Optional<ProfitResponseDto> getProfitByMonth(String month) {
        try {
            String url = profitServiceUrl + "/api/profit/month/" + month;
            log.info("Calling profit service: {}", url);
            
            ResponseEntity<ProfitResponseDto> response = restTemplate.getForEntity(url, ProfitResponseDto.class);
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            log.error("Error calling profit service for month {}: {}", month, e.getMessage());
            return Optional.empty();
        }
    }
    
    public List<ProfitResponseDto> getAllProfits() {
        try {
            String url = profitServiceUrl + "/api/profit/all";
            log.info("Calling profit service: {}", url);
            
            ResponseEntity<List<ProfitResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProfitResponseDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling profit service for all profits: {}", e.getMessage());
            return List.of();
        }
    }
    
    public List<ProfitResponseDto> getProfitsByYear(int year) {
        try {
            String url = profitServiceUrl + "/api/profit/year/" + year;
            log.info("Calling profit service: {}", url);
            
            ResponseEntity<List<ProfitResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProfitResponseDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling profit service for year {}: {}", year, e.getMessage());
            return List.of();
        }
    }
    
    public BigDecimal getTotalProfitForYear(int year) {
        try {
            String url = profitServiceUrl + "/api/profit/year/" + year + "/total";
            log.info("Calling profit service: {}", url);
            
            ResponseEntity<BigDecimal> response = restTemplate.getForEntity(url, BigDecimal.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling profit service for year {} total: {}", year, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
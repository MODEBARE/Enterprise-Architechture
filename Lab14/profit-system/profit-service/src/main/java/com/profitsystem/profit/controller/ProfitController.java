package com.profitsystem.profit.controller;

import com.profitsystem.profit.dto.ProfitResponseDto;
import com.profitsystem.profit.service.ProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profit")
@CrossOrigin(origins = "*")
public class ProfitController {
    
    @Autowired
    private ProfitService profitService;
    
    @GetMapping("/month/{month}")
    public ResponseEntity<ProfitResponseDto> getProfitByMonth(@PathVariable String month) {
        Optional<ProfitResponseDto> profit = profitService.getProfitByMonth(month);
        return profit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<ProfitResponseDto>> getAllProfits() {
        List<ProfitResponseDto> profits = profitService.getAllProfits();
        return ResponseEntity.ok(profits);
    }
    
    @GetMapping("/year/{year}")
    public ResponseEntity<List<ProfitResponseDto>> getProfitsByYear(@PathVariable int year) {
        List<ProfitResponseDto> profits = profitService.getProfitsByYear(year);
        return ResponseEntity.ok(profits);
    }
    
    @GetMapping("/year/{year}/total")
    public ResponseEntity<BigDecimal> getTotalProfitForYear(@PathVariable int year) {
        BigDecimal total = profitService.getTotalProfitForYear(year);
        return ResponseEntity.ok(total);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Profit Service is running");
    }
}
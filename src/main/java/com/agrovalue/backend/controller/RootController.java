package com.agrovalue.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("status", "success");
        response.put("message", "Welcome to AgroValue Connect API");
        response.put("version", "1.0.0");
        response.put("documentation", "https://agrovalue-backend.onrender.com/api/docs");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("auth", "/auth/login, /auth/register");
        endpoints.put("products", "/api/products");
        endpoints.put("cart", "/api/cart/{userId}");
        endpoints.put("orders", "/api/orders");
        response.put("endpoints", endpoints);
        
        response.put("health", "/health");
        
        return response;
    }
    
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> healthResponse = new HashMap<>();
        healthResponse.put("status", "UP");
        healthResponse.put("service", "AgroValue Connect API");
        healthResponse.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return healthResponse;
    }
}
package com.agrovalue.backend.controller;

import com.agrovalue.backend.entity.User;
import com.agrovalue.backend.repository.UserRepository;
import com.agrovalue.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/oauth2")
public class OAuth2Controller {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email).orElse(null);
            
            if (user != null) {
                response.put("id", user.getId());
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("imageUrl", user.getImageUrl());
                response.put("verified", user.isVerified());
                response.put("provider", user.getProvider());
                
                if (user.getRoles() != null) {
                    response.put("roles", user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList());
                }
            }
        }
        
        return ResponseEntity.ok(response);
    }
}
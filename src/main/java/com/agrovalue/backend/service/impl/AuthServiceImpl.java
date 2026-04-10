package com.agrovalue.backend.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agrovalue.backend.dto.AuthResponse;
import com.agrovalue.backend.dto.RegisterRequest;
import com.agrovalue.backend.entity.Role;
import com.agrovalue.backend.entity.User;
import com.agrovalue.backend.repository.RoleRepository;
import com.agrovalue.backend.repository.UserRepository;
import com.agrovalue.backend.security.JwtUtil;
import com.agrovalue.backend.service.AuthService;
import com.agrovalue.backend.service.EmailService;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // user is active immediately
        user.setVerified(true);

        String roleName = "ROLE_" + request.getRole().toUpperCase();

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        user.setRoles(Set.of(role));

        userRepository.save(user);

        // send welcome email
        emailService.sendVerificationEmail(user.getEmail(), null);

        return "User registered successfully";
    }

    @Override
    public String verifyUser(String token) {
        // verification feature removed
        return "Email verification disabled";
    }

    @Override
    public AuthResponse login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        user.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                                .toList()
                )
        );

        String role = user.getRoles().iterator().next().getName();
        // Remove "ROLE_" prefix for frontend
        String cleanRole = role.replace("ROLE_", "");

        // Return AuthResponse with id, name, email, role, token
        return new AuthResponse(
            token,
            user.getId(),      
            user.getName(),    
            user.getEmail(),   
            cleanRole          
        );
    }
}
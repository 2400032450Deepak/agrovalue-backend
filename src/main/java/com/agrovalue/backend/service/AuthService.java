package com.agrovalue.backend.service;

import com.agrovalue.backend.dto.AuthResponse;
import com.agrovalue.backend.dto.RegisterRequest;

public interface AuthService {

    String register(RegisterRequest request);

    String verifyUser(String token);

    AuthResponse login(String email, String password);
}
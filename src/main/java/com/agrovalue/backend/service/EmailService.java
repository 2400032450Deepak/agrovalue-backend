package com.agrovalue.backend.service;

public interface EmailService {

    void sendVerificationEmail(String toEmail, String token);
}
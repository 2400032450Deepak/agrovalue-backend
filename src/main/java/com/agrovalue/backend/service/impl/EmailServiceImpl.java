package com.agrovalue.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.agrovalue.backend.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {

        String subject = "Welcome to AgroValue Connect 🌾";

        String message =
                "Hello,\n\n" +
                "Welcome to AgroValue Connect!\n\n" +
                "Your account has been successfully created.\n" +
                "You can now login and start using the platform.\n\n" +
                "Thank you for joining us!\n\n" +
                "Best Regards,\n" +
                "AgroValue Team";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(message);

        mailSender.send(mail);
    }
}
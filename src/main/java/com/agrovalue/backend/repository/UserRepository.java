package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationToken(String token);
    
    // OAuth2 methods
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
    
    Optional<User> findByProviderAndEmail(String provider, String email);
}
package com.agrovalue.backend.security;

import com.agrovalue.backend.config.CustomOAuth2User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret:mysecretkeymysecretkeymysecretkey}")
    private String SECRET;

    @Value("${jwt.expiration:86400000}")
    private int jwtExpiration;

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // 🔥 MAIN METHOD (USE THIS ONLY)
    public String generateToken(Authentication authentication) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        String email;
        String role;

        if (authentication.getPrincipal() instanceof CustomOAuth2User) {
            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
            email = user.getEmail();
            role = user.getAuthorities().iterator().next().getAuthority();
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            email = user.getUsername();
            role = user.getAuthorities().iterator().next().getAuthority();
        } else {
            email = authentication.getName();
            role = "ROLE_USER";
        }

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role) // 🔥 IMPORTANT
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 🔥 EXTRACT EMAIL
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // 🔥 EXTRACT ROLE
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // 🔥 VALIDATE TOKEN
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
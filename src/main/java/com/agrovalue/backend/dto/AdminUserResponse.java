package com.agrovalue.backend.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AdminUserResponse {
    private Long id;
    private String name;
    private String email;
    private boolean verified;
    private String status;
    private Set<String> roles;
}

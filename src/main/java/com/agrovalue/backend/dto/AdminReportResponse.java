package com.agrovalue.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminReportResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;
}

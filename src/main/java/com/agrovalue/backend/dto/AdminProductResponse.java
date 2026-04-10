package com.agrovalue.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long farmerId;
    private String farmerName;
    private String status;
    private LocalDateTime createdAt;
}

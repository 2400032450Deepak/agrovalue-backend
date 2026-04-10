package com.agrovalue.backend.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminTransactionResponse {
    private Long orderId;
    private Long userId;
    private String userEmail;
    private String orderStatus;
    private BigDecimal totalAmount;
    private LocalDateTime orderedAt;
}

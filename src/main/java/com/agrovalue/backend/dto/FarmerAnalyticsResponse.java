package com.agrovalue.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FarmerAnalyticsResponse {
    private Long farmerId;
    private String farmerName;
    private String farmerEmail;
    private long totalProducts;
    private long totalOrders;
    private BigDecimal totalRevenue;
}

package com.agrovalue.backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalFarmers;
    private long totalBuyers;
    private long totalProducts;
    private long activeProducts;
    private long pendingProducts;
    private BigDecimal totalRevenue;
    private long totalOrders;
    private long activeSessions;
    private long issuesCount;
}

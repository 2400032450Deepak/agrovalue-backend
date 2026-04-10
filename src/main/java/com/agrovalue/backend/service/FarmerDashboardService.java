package com.agrovalue.backend.service;

import java.util.List;

import com.agrovalue.backend.dto.DashboardStatsResponse;
import com.agrovalue.backend.dto.OrderResponse;
import com.agrovalue.backend.dto.ProductResponse;

public interface FarmerDashboardService {
    List<ProductResponse> getFarmerProducts(Long farmerId);
    List<OrderResponse> getFarmerOrders(Long farmerId);
    DashboardStatsResponse getDashboardStats(Long farmerId);
}

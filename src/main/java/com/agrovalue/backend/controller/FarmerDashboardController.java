package com.agrovalue.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agrovalue.backend.dto.DashboardStatsResponse;
import com.agrovalue.backend.dto.OrderResponse;
import com.agrovalue.backend.dto.ProductResponse;
import com.agrovalue.backend.service.FarmerDashboardService;

@PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')")
@RestController
@RequestMapping("/api/farmers")
public class FarmerDashboardController {

    private final FarmerDashboardService farmerDashboardService;

    public FarmerDashboardController(FarmerDashboardService farmerDashboardService) {
        this.farmerDashboardService = farmerDashboardService;
    }

    @GetMapping("/{farmerId}/products")
    public ResponseEntity<List<ProductResponse>> getFarmerProducts(@PathVariable Long farmerId) {
        return ResponseEntity.ok(farmerDashboardService.getFarmerProducts(farmerId));
    }

    @GetMapping("/{farmerId}/orders")
    public ResponseEntity<List<OrderResponse>> getFarmerOrders(@PathVariable Long farmerId) {
        return ResponseEntity.ok(farmerDashboardService.getFarmerOrders(farmerId));
    }

    @GetMapping("/{farmerId}/dashboard-stats")
    public ResponseEntity<DashboardStatsResponse> getDashboardStats(@PathVariable Long farmerId) {
        return ResponseEntity.ok(farmerDashboardService.getDashboardStats(farmerId));
    }
}

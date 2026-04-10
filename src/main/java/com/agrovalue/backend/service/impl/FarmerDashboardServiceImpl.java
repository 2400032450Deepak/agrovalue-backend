package com.agrovalue.backend.service.impl;

import org.springframework.stereotype.Service;

import com.agrovalue.backend.dto.DashboardStatsResponse;
import com.agrovalue.backend.dto.OrderResponse;
import com.agrovalue.backend.dto.ProductResponse;
import com.agrovalue.backend.repository.OrderRepository;
import com.agrovalue.backend.repository.ProductRepository;
import com.agrovalue.backend.repository.ReviewRepository;
import com.agrovalue.backend.service.FarmerDashboardService;
import com.agrovalue.backend.service.OrderService;
import com.agrovalue.backend.service.ProductService;

import java.util.List;

@Service
public class FarmerDashboardServiceImpl implements FarmerDashboardService {

    private final ProductService productService;
    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public FarmerDashboardServiceImpl(ProductService productService, OrderService orderService, ProductRepository productRepository, OrderRepository orderRepository, ReviewRepository reviewRepository) {
        this.productService = productService;
        this.orderService = orderService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<ProductResponse> getFarmerProducts(Long farmerId) {
        return productService.getProductsByFarmer(farmerId);
    }

    @Override
    public List<OrderResponse> getFarmerOrders(Long farmerId) {
        return orderService.getFarmerOrders(farmerId);
    }

    @Override
    public DashboardStatsResponse getDashboardStats(Long farmerId) {
        DashboardStatsResponse response = new DashboardStatsResponse();
        response.setTotalProducts(productRepository.countByFarmerId(farmerId));
        response.setTotalOrders(orderRepository.countOrdersByFarmerId(farmerId));
        response.setLowStockProducts(productRepository.countByFarmerIdAndStockLessThan(farmerId, 10));
        Double avg = reviewRepository.findAverageRatingByFarmerId(farmerId);
        response.setAverageRating(avg == null ? 0.0 : Math.round(avg * 10.0) / 10.0);
        return response;
    }
}

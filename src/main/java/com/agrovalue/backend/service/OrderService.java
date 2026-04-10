package com.agrovalue.backend.service;

import java.util.List;

import com.agrovalue.backend.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(Long userId);
    List<OrderResponse> getUserOrders(Long userId);
    OrderResponse getOrderDetails(Long orderId);
    List<OrderResponse> getFarmerOrders(Long farmerId);
}

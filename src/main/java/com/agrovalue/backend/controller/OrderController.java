package com.agrovalue.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.agrovalue.backend.dto.OrderResponse;
import com.agrovalue.backend.dto.PlaceOrderRequest;
import com.agrovalue.backend.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 🔥 PLACE ORDER → ONLY BUYER
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {

        OrderResponse response = orderService.placeOrder(request.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 🔥 GET USER ORDERS → BUYER OR ADMIN
    @PreAuthorize("hasAnyRole('BUYER','ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {

        List<OrderResponse> orders = orderService.getUserOrders(userId);

        return ResponseEntity.ok(orders);
    }

    // 🔥 GET ORDER DETAILS → BUYER OR ADMIN
    @PreAuthorize("hasAnyRole('BUYER','ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable Long orderId) {

        OrderResponse order = orderService.getOrderDetails(orderId);

        return ResponseEntity.ok(order);
    }
}
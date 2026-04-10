package com.agrovalue.backend.service;

import com.agrovalue.backend.dto.CartItemRequest;
import com.agrovalue.backend.dto.CartResponse;

public interface CartService {
    CartResponse addToCart(CartItemRequest request);
    CartResponse updateQuantity(Long cartItemId, Integer quantity);
    void removeItem(Long cartItemId);
    CartResponse getUserCart(Long userId);
}

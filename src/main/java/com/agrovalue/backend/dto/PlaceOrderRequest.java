package com.agrovalue.backend.dto;

import jakarta.validation.constraints.NotNull;

public class PlaceOrderRequest {
    @NotNull
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

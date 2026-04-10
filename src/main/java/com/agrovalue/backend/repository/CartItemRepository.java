package com.agrovalue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agrovalue.backend.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItem> findByCartId(Long cartId);
}

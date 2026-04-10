package com.agrovalue.backend.service.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agrovalue.backend.dto.CartItemRequest;
import com.agrovalue.backend.dto.CartItemResponse;
import com.agrovalue.backend.dto.CartResponse;
import com.agrovalue.backend.entity.Cart;
import com.agrovalue.backend.entity.CartItem;
import com.agrovalue.backend.entity.Product;
import com.agrovalue.backend.entity.User;
import com.agrovalue.backend.exception.BadRequestException;
import com.agrovalue.backend.exception.ResourceNotFoundException;
import com.agrovalue.backend.repository.CartItemRepository;
import com.agrovalue.backend.repository.CartRepository;
import com.agrovalue.backend.repository.ProductRepository;
import com.agrovalue.backend.repository.UserRepository;
import com.agrovalue.backend.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CartResponse addToCart(CartItemRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        if (product.getStock() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        CartItem item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        int newQty = item.getQuantity() + request.getQuantity();

        if (product.getStock() < newQty) {
            throw new BadRequestException("Requested quantity exceeds stock for product: " + product.getName());
        }

        item.setQuantity(newQty);
        cartItemRepository.save(item);

        return getUserCart(user.getId());
    }

    @Override
    @Transactional
    public CartResponse updateQuantity(Long cartItemId, Integer quantity) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        if (quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        if (item.getProduct().getStock() < quantity) {
            throw new BadRequestException("Insufficient stock for product: " + item.getProduct().getName());
        }

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return getUserCart(item.getCart().getUser().getId());
    }

    @Override
    @Transactional
    public void removeItem(Long cartItemId) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));

        cartItemRepository.delete(item);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getUserCart(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            CartResponse emptyResponse = new CartResponse();
            emptyResponse.setCartId(null);
            emptyResponse.setUserId(userId);
            emptyResponse.setItems(Collections.emptyList());
            emptyResponse.setTotalAmount(BigDecimal.ZERO);
            return emptyResponse;
        }

        List<CartItemResponse> itemResponses = cartItemRepository.findByCartId(cart.getId())
                .stream()
                .map(item -> {
                    CartItemResponse response = new CartItemResponse();
                    response.setCartItemId(item.getId());
                    response.setProductId(item.getProduct().getId());
                    response.setProductName(item.getProduct().getName());
                    response.setUnitPrice(item.getProduct().getPrice());
                    response.setQuantity(item.getQuantity());
                    response.setLineTotal(
                            item.getProduct().getPrice()
                                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                    return response;
                })
                .collect(Collectors.toList());

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartResponse response = new CartResponse();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setItems(itemResponses);
        response.setTotalAmount(total);

        return response;
    }
}
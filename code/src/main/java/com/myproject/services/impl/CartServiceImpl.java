package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.models.dtos.CartItem;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItemEntity;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.CartServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CartService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartServiceInterface {
    
    private final CartRepository cartRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Cart findCartById(Long cartId) {
        log.debug("Finding cart by ID: {}", cartId);
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cart findCartByUserId(String userId) {
        log.debug("Finding cart by user ID: {}", userId);
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found for user: " + userId));
    }
    
    @Override
    @Transactional
    public Cart saveCart(Cart cart) {
        log.debug("Saving cart: {}", cart.getId());
        return cartRepository.save(cart);
    }
    
    @Override
    public CartResponse toCartResponse(Cart cart) {
        List<CartItem> items = cart.getItems().stream()
                .map(this::toCartItem)
                .collect(Collectors.toList());
        
        return CartResponse.builder()
                .id(cart.getId() != null ? cart.getId().toString() : null)
                .userId(cart.getUserId())
                .items(items)
                .total(cart.getTotal())
                .originalTotal(cart.getOriginalTotal())
                .appliedCoupon(cart.getAppliedCouponCode())
                .discountAmount(cart.getDiscountAmount())
                .build();
    }
    
    @Override
    public void calculateCartTotal(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        
        cart.setOriginalTotal(total);
        cart.setTotal(total);
        
        log.debug("Calculated cart total: {}", total);
    }
    
    private CartItem toCartItem(CartItemEntity entity) {
        return CartItem.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .price(entity.getPrice())
                .build();
    }
}

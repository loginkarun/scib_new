package com.myproject.controllers;

import com.myproject.models.dtos.ApplyCouponRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.CartCouponServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for cart coupon operations
 * Handles applying and removing coupons from cart
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartCouponController {
    
    private final CartCouponServiceInterface cartCouponService;
    
    /**
     * Apply coupon to cart
     * POST /api/cart/{cartId}/coupon
     */
    @PostMapping("/{cartId}/coupon")
    public ResponseEntity<CartResponse> applyCoupon(
            @PathVariable Long cartId,
            @Valid @RequestBody ApplyCouponRequest request) {
        
        log.info("Received request to apply coupon to cart: {}", cartId);
        CartResponse response = cartCouponService.applyCoupon(cartId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * Remove coupon from cart
     * DELETE /api/cart/{cartId}/coupon
     */
    @DeleteMapping("/{cartId}/coupon")
    public ResponseEntity<Void> removeCoupon(@PathVariable Long cartId) {
        log.info("Received request to remove coupon from cart: {}", cartId);
        cartCouponService.removeCoupon(cartId);
        return ResponseEntity.noContent().build();
    }
}

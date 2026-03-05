package com.myproject.services.impl;

import com.myproject.exceptions.CouponExpiredException;
import com.myproject.exceptions.CouponNotApplicableException;
import com.myproject.exceptions.CouponNotFoundException;
import com.myproject.models.dtos.CouponValidationResult;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.Coupon;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.CouponRepository;
import com.myproject.services.CouponServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Implementation of CouponService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponServiceInterface {
    
    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Coupon findCouponByCode(String code) {
        log.debug("Finding coupon by code: {}", code);
        return couponRepository.findById(code)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found: " + code));
    }
    
    @Override
    @Transactional(readOnly = true)
    public CouponValidationResult validateCoupon(String couponCode, String cartId) {
        log.debug("Validating coupon {} for cart {}", couponCode, cartId);
        
        try {
            Coupon coupon = findCouponByCode(couponCode);
            Cart cart = cartRepository.findById(Long.parseLong(cartId))
                    .orElseThrow(() -> new CouponNotFoundException("Cart not found: " + cartId));
            
            boolean isValid = isCouponValid(coupon);
            boolean isApplicable = isCouponApplicableToCart(coupon, cart);
            
            String message;
            if (!isValid) {
                message = "Coupon has expired";
            } else if (!isApplicable) {
                message = "Coupon is not applicable to this cart";
            } else {
                message = "Coupon is valid and applicable";
            }
            
            return CouponValidationResult.builder()
                    .valid(isValid && isApplicable)
                    .couponCode(coupon.getCode())
                    .discountType(coupon.getDiscountType().name())
                    .discountValue(coupon.getDiscountValue())
                    .minCartValue(coupon.getMinCartValue())
                    .expiryDate(coupon.getExpiryDate())
                    .message(message)
                    .applicableItems(coupon.getApplicableItems() != null ? coupon.getApplicableItems() : new ArrayList<>())
                    .build();
        } catch (Exception e) {
            log.error("Error validating coupon: {}", e.getMessage());
            return CouponValidationResult.builder()
                    .valid(false)
                    .couponCode(couponCode)
                    .message("Coupon validation failed: " + e.getMessage())
                    .applicableItems(new ArrayList<>())
                    .build();
        }
    }
    
    @Override
    public boolean isCouponValid(Coupon coupon) {
        boolean isValid = coupon.getExpiryDate().isAfter(LocalDate.now()) || 
                         coupon.getExpiryDate().isEqual(LocalDate.now());
        
        if (!isValid) {
            log.warn("Coupon {} has expired", coupon.getCode());
        }
        
        return isValid;
    }
    
    @Override
    public boolean isCouponApplicableToCart(Coupon coupon, Cart cart) {
        // Check minimum cart value
        if (coupon.getMinCartValue() != null && cart.getTotal() < coupon.getMinCartValue()) {
            log.warn("Cart total {} is less than minimum required {}", cart.getTotal(), coupon.getMinCartValue());
            return false;
        }
        
        // Check if coupon applies to specific items
        if (coupon.getApplicableItems() != null && !coupon.getApplicableItems().isEmpty()) {
            boolean hasApplicableItem = cart.getItems().stream()
                    .anyMatch(item -> coupon.getApplicableItems().contains(item.getProductId()));
            
            if (!hasApplicableItem) {
                log.warn("Cart does not contain any items applicable for coupon {}", coupon.getCode());
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public Double calculateDiscount(Coupon coupon, Double cartTotal) {
        double discount = 0.0;
        
        switch (coupon.getDiscountType()) {
            case PERCENTAGE:
                discount = (cartTotal * coupon.getDiscountValue()) / 100.0;
                break;
            case FIXED_AMOUNT:
                discount = coupon.getDiscountValue();
                break;
        }
        
        // Ensure discount doesn't exceed cart total
        discount = Math.min(discount, cartTotal);
        
        log.debug("Calculated discount: {} for cart total: {}", discount, cartTotal);
        return discount;
    }
}

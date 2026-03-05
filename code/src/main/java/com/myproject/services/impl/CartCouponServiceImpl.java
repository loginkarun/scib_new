package com.myproject.services.impl;

import com.myproject.exceptions.CouponExpiredException;
import com.myproject.exceptions.CouponNotApplicableException;
import com.myproject.models.dtos.ApplyCouponRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.Coupon;
import com.myproject.services.CartCouponServiceInterface;
import com.myproject.services.CartServiceInterface;
import com.myproject.services.CouponServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CartCouponService
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartCouponServiceImpl implements CartCouponServiceInterface {
    
    private final CartServiceInterface cartService;
    private final CouponServiceInterface couponService;
    
    @Override
    @Transactional
    public CartResponse applyCoupon(Long cartId, ApplyCouponRequest request) {
        log.info("Applying coupon {} to cart {}", request.getCouponCode(), cartId);
        
        // Find cart
        Cart cart = cartService.findCartById(cartId);
        
        // Find and validate coupon
        Coupon coupon = couponService.findCouponByCode(request.getCouponCode());
        
        if (!couponService.isCouponValid(coupon)) {
            throw new CouponExpiredException("Coupon code invalid or expired");
        }
        
        if (!couponService.isCouponApplicableToCart(coupon, cart)) {
            throw new CouponNotApplicableException("Coupon is not applicable to this cart");
        }
        
        // Calculate discount
        Double discount = couponService.calculateDiscount(coupon, cart.getOriginalTotal());
        
        // Apply coupon to cart
        cart.setAppliedCouponCode(coupon.getCode());
        cart.setDiscountAmount(discount);
        cart.setTotal(cart.getOriginalTotal() - discount);
        
        // Save cart
        Cart savedCart = cartService.saveCart(cart);
        
        log.info("Coupon {} applied successfully to cart {}. Discount: {}", 
                coupon.getCode(), cartId, discount);
        
        return cartService.toCartResponse(savedCart);
    }
    
    @Override
    @Transactional
    public void removeCoupon(Long cartId) {
        log.info("Removing coupon from cart {}", cartId);
        
        // Find cart
        Cart cart = cartService.findCartById(cartId);
        
        // Remove coupon
        cart.setAppliedCouponCode(null);
        cart.setDiscountAmount(0.0);
        cart.setTotal(cart.getOriginalTotal());
        
        // Save cart
        cartService.saveCart(cart);
        
        log.info("Coupon removed successfully from cart {}", cartId);
    }
}

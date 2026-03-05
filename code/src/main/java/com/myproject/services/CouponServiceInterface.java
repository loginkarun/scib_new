package com.myproject.services;

import com.myproject.models.dtos.CouponValidationResult;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.Coupon;

/**
 * Service interface for coupon operations
 */
public interface CouponServiceInterface {
    
    Coupon findCouponByCode(String code);
    
    CouponValidationResult validateCoupon(String couponCode, String cartId);
    
    boolean isCouponValid(Coupon coupon);
    
    boolean isCouponApplicableToCart(Coupon coupon, Cart cart);
    
    Double calculateDiscount(Coupon coupon, Double cartTotal);
}

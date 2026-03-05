package com.myproject.services;

import com.myproject.models.dtos.ApplyCouponRequest;
import com.myproject.models.dtos.CartResponse;

/**
 * Service interface for cart coupon operations
 */
public interface CartCouponServiceInterface {
    
    CartResponse applyCoupon(Long cartId, ApplyCouponRequest request);
    
    void removeCoupon(Long cartId);
}

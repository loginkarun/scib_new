package com.myproject.exceptions;

/**
 * Exception thrown when coupon is not found
 */
public class CouponNotFoundException extends RuntimeException {
    
    public CouponNotFoundException(String message) {
        super(message);
    }
    
    public CouponNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

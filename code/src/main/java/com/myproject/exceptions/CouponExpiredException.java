package com.myproject.exceptions;

/**
 * Exception thrown when coupon has expired
 */
public class CouponExpiredException extends RuntimeException {
    
    public CouponExpiredException(String message) {
        super(message);
    }
    
    public CouponExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}

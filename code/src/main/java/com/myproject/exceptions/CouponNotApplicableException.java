package com.myproject.exceptions;

/**
 * Exception thrown when coupon is not applicable to cart
 */
public class CouponNotApplicableException extends RuntimeException {
    
    public CouponNotApplicableException(String message) {
        super(message);
    }
    
    public CouponNotApplicableException(String message, Throwable cause) {
        super(message, cause);
    }
}

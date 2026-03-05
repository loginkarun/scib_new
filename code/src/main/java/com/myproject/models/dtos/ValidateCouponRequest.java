package com.myproject.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for validating coupon request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCouponRequest {
    
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
    
    @NotBlank(message = "Cart ID is required")
    private String cartId;
}
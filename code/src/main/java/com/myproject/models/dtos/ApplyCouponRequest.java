package com.myproject.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for applying coupon to cart request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCouponRequest {
    
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
}
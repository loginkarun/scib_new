package com.myproject.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for applying coupon to cart
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyCouponRequest {
    
    @NotBlank(message = "Coupon code is required")
    private String couponCode;
}

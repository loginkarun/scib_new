package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for coupon validation result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponValidationResult {
    
    private Boolean valid;
    private String couponCode;
    private String discountType;
    private Double discountValue;
    private Double minCartValue;
    private LocalDate expiryDate;
    private String message;
    private List<String> applicableItems;
}

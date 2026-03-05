package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for cart response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    
    private String id;
    private String userId;
    private List<CartItem> items;
    private Double total;
    private Double originalTotal;
    private String appliedCoupon;
    private Double discountAmount;
}

package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for cart item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    private String id;
    private String productId;
    private Integer quantity;
    private Double price;
}

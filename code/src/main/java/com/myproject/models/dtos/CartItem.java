package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a cart item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    private String id;
    private String productId;
    private Integer quantity;
    private Double price;
}
package com.myproject.services;

import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;

/**
 * Service interface for cart operations
 */
public interface CartServiceInterface {
    
    Cart findCartById(Long cartId);
    
    Cart findCartByUserId(String userId);
    
    Cart saveCart(Cart cart);
    
    CartResponse toCartResponse(Cart cart);
    
    void calculateCartTotal(Cart cart);
}

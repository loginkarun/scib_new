package com.myproject.services;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItemEntity;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    
    @Mock
    private CartRepository cartRepository;
    
    @InjectMocks
    private CartServiceImpl cartService;
    
    private Cart testCart;
    private List<CartItemEntity> testItems;
    
    @BeforeEach
    void setUp() {
        testItems = new ArrayList<>();
        CartItemEntity item1 = CartItemEntity.builder()
                .id(1L)
                .productId("prod1")
                .quantity(2)
                .price(50.0)
                .build();
        testItems.add(item1);
        
        testCart = Cart.builder()
                .id(1L)
                .userId("user123")
                .items(testItems)
                .total(100.0)
                .originalTotal(100.0)
                .build();
        
        // Set cart reference for items
        testItems.forEach(item -> item.setCart(testCart));
    }
    
    @Test
    void testFindCartById_Success() {
        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        
        Cart result = cartService.findCartById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).findById(1L);
    }
    
    @Test
    void testFindCartById_NotFound() {
        when(cartRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(CartNotFoundException.class, () -> {
            cartService.findCartById(999L);
        });
        
        verify(cartRepository, times(1)).findById(999L);
    }
    
    @Test
    void testFindCartByUserId_Success() {
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(testCart));
        
        Cart result = cartService.findCartByUserId("user123");
        
        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        verify(cartRepository, times(1)).findByUserId("user123");
    }
    
    @Test
    void testSaveCart_Success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);
        
        Cart result = cartService.saveCart(testCart);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(cartRepository, times(1)).save(testCart);
    }
    
    @Test
    void testToCartResponse_Success() {
        CartResponse response = cartService.toCartResponse(testCart);
        
        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("user123", response.getUserId());
        assertEquals(1, response.getItems().size());
        assertEquals(100.0, response.getTotal());
    }
    
    @Test
    void testCalculateCartTotal_Success() {
        cartService.calculateCartTotal(testCart);
        
        assertEquals(100.0, testCart.getOriginalTotal());
        assertEquals(100.0, testCart.getTotal());
    }
}

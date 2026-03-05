package com.myproject.services;

import com.myproject.exceptions.CouponExpiredException;
import com.myproject.models.dtos.ApplyCouponRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItemEntity;
import com.myproject.models.entities.Coupon;
import com.myproject.services.impl.CartCouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartCouponServiceImplTest {
    
    @Mock
    private CartServiceInterface cartService;
    
    @Mock
    private CouponServiceInterface couponService;
    
    @InjectMocks
    private CartCouponServiceImpl cartCouponService;
    
    private Cart testCart;
    private Coupon testCoupon;
    private ApplyCouponRequest request;
    
    @BeforeEach
    void setUp() {
        List<CartItemEntity> items = new ArrayList<>();
        CartItemEntity item = CartItemEntity.builder()
                .id(1L)
                .productId("prod1")
                .quantity(2)
                .price(50.0)
                .build();
        items.add(item);
        
        testCart = Cart.builder()
                .id(1L)
                .userId("user123")
                .items(items)
                .total(100.0)
                .originalTotal(100.0)
                .build();
        
        items.forEach(i -> i.setCart(testCart));
        
        testCoupon = Coupon.builder()
                .code("SAVE20")
                .discountType(Coupon.DiscountType.PERCENTAGE)
                .discountValue(20.0)
                .expiryDate(LocalDate.now().plusDays(30))
                .minCartValue(50.0)
                .applicableItems(new ArrayList<>())
                .build();
        
        request = ApplyCouponRequest.builder()
                .couponCode("SAVE20")
                .build();
    }
    
    @Test
    void testApplyCoupon_Success() {
        when(cartService.findCartById(1L)).thenReturn(testCart);
        when(couponService.findCouponByCode("SAVE20")).thenReturn(testCoupon);
        when(couponService.isCouponValid(testCoupon)).thenReturn(true);
        when(couponService.isCouponApplicableToCart(testCoupon, testCart)).thenReturn(true);
        when(couponService.calculateDiscount(testCoupon, 100.0)).thenReturn(20.0);
        when(cartService.saveCart(any(Cart.class))).thenReturn(testCart);
        when(cartService.toCartResponse(any(Cart.class))).thenReturn(CartResponse.builder()
                .id("1")
                .total(80.0)
                .appliedCoupon("SAVE20")
                .discountAmount(20.0)
                .build());
        
        CartResponse response = cartCouponService.applyCoupon(1L, request);
        
        assertNotNull(response);
        assertEquals("SAVE20", response.getAppliedCoupon());
        assertEquals(20.0, response.getDiscountAmount());
        verify(cartService, times(1)).saveCart(any(Cart.class));
    }
    
    @Test
    void testApplyCoupon_ExpiredCoupon() {
        when(cartService.findCartById(1L)).thenReturn(testCart);
        when(couponService.findCouponByCode("SAVE20")).thenReturn(testCoupon);
        when(couponService.isCouponValid(testCoupon)).thenReturn(false);
        
        assertThrows(CouponExpiredException.class, () -> {
            cartCouponService.applyCoupon(1L, request);
        });
        
        verify(cartService, never()).saveCart(any(Cart.class));
    }
    
    @Test
    void testRemoveCoupon_Success() {
        testCart.setAppliedCouponCode("SAVE20");
        testCart.setDiscountAmount(20.0);
        testCart.setTotal(80.0);
        
        when(cartService.findCartById(1L)).thenReturn(testCart);
        when(cartService.saveCart(any(Cart.class))).thenReturn(testCart);
        
        cartCouponService.removeCoupon(1L);
        
        verify(cartService, times(1)).saveCart(any(Cart.class));
    }
}

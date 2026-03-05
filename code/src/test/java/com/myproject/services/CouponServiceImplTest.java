package com.myproject.services;

import com.myproject.exceptions.CouponNotFoundException;
import com.myproject.models.dtos.CouponValidationResult;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItemEntity;
import com.myproject.models.entities.Coupon;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.CouponRepository;
import com.myproject.services.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {
    
    @Mock
    private CouponRepository couponRepository;
    
    @Mock
    private CartRepository cartRepository;
    
    @InjectMocks
    private CouponServiceImpl couponService;
    
    private Coupon testCoupon;
    private Cart testCart;
    
    @BeforeEach
    void setUp() {
        testCoupon = Coupon.builder()
                .code("SAVE20")
                .discountType(Coupon.DiscountType.PERCENTAGE)
                .discountValue(20.0)
                .expiryDate(LocalDate.now().plusDays(30))
                .minCartValue(50.0)
                .applicableItems(new ArrayList<>())
                .build();
        
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
    }
    
    @Test
    void testFindCouponByCode_Success() {
        when(couponRepository.findById("SAVE20")).thenReturn(Optional.of(testCoupon));
        
        Coupon result = couponService.findCouponByCode("SAVE20");
        
        assertNotNull(result);
        assertEquals("SAVE20", result.getCode());
        verify(couponRepository, times(1)).findById("SAVE20");
    }
    
    @Test
    void testFindCouponByCode_NotFound() {
        when(couponRepository.findById("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(CouponNotFoundException.class, () -> {
            couponService.findCouponByCode("INVALID");
        });
        
        verify(couponRepository, times(1)).findById("INVALID");
    }
    
    @Test
    void testIsCouponValid_ValidCoupon() {
        boolean result = couponService.isCouponValid(testCoupon);
        
        assertTrue(result);
    }
    
    @Test
    void testIsCouponValid_ExpiredCoupon() {
        testCoupon.setExpiryDate(LocalDate.now().minusDays(1));
        
        boolean result = couponService.isCouponValid(testCoupon);
        
        assertFalse(result);
    }
    
    @Test
    void testIsCouponApplicableToCart_Success() {
        boolean result = couponService.isCouponApplicableToCart(testCoupon, testCart);
        
        assertTrue(result);
    }
    
    @Test
    void testIsCouponApplicableToCart_BelowMinValue() {
        testCoupon.setMinCartValue(200.0);
        
        boolean result = couponService.isCouponApplicableToCart(testCoupon, testCart);
        
        assertFalse(result);
    }
    
    @Test
    void testCalculateDiscount_Percentage() {
        Double discount = couponService.calculateDiscount(testCoupon, 100.0);
        
        assertEquals(20.0, discount);
    }
    
    @Test
    void testCalculateDiscount_FixedAmount() {
        testCoupon.setDiscountType(Coupon.DiscountType.FIXED_AMOUNT);
        testCoupon.setDiscountValue(15.0);
        
        Double discount = couponService.calculateDiscount(testCoupon, 100.0);
        
        assertEquals(15.0, discount);
    }
    
    @Test
    void testValidateCoupon_Success() {
        when(couponRepository.findById("SAVE20")).thenReturn(Optional.of(testCoupon));
        when(cartRepository.findById(1L)).thenReturn(Optional.of(testCart));
        
        CouponValidationResult result = couponService.validateCoupon("SAVE20", "1");
        
        assertNotNull(result);
        assertTrue(result.getValid());
        assertEquals("SAVE20", result.getCouponCode());
        assertEquals("Coupon is valid and applicable", result.getMessage());
    }
}

package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.ApplyCouponRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.CartCouponServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartCouponController.class)
class CartCouponControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CartCouponServiceInterface cartCouponService;
    
    private ApplyCouponRequest request;
    private CartResponse response;
    
    @BeforeEach
    void setUp() {
        request = ApplyCouponRequest.builder()
                .couponCode("SAVE20")
                .build();
        
        response = CartResponse.builder()
                .id("1")
                .userId("user123")
                .total(80.0)
                .originalTotal(100.0)
                .appliedCoupon("SAVE20")
                .discountAmount(20.0)
                .build();
    }
    
    @Test
    void testApplyCoupon_Success() throws Exception {
        when(cartCouponService.applyCoupon(anyLong(), any(ApplyCouponRequest.class)))
                .thenReturn(response);
        
        mockMvc.perform(post("/cart/1/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.appliedCoupon").value("SAVE20"))
                .andExpect(jsonPath("$.discountAmount").value(20.0));
        
        verify(cartCouponService, times(1)).applyCoupon(anyLong(), any(ApplyCouponRequest.class));
    }
    
    @Test
    void testApplyCoupon_InvalidRequest() throws Exception {
        ApplyCouponRequest invalidRequest = ApplyCouponRequest.builder()
                .couponCode("")
                .build();
        
        mockMvc.perform(post("/cart/1/coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(cartCouponService, never()).applyCoupon(anyLong(), any(ApplyCouponRequest.class));
    }
    
    @Test
    void testRemoveCoupon_Success() throws Exception {
        doNothing().when(cartCouponService).removeCoupon(anyLong());
        
        mockMvc.perform(delete("/cart/1/coupon"))
                .andExpect(status().isNoContent());
        
        verify(cartCouponService, times(1)).removeCoupon(1L);
    }
}

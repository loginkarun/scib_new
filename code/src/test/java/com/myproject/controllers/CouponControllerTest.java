package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.CouponValidationResult;
import com.myproject.models.dtos.ValidateCouponRequest;
import com.myproject.services.CouponServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private CouponServiceInterface couponService;
    
    private ValidateCouponRequest request;
    private CouponValidationResult result;
    
    @BeforeEach
    void setUp() {
        request = ValidateCouponRequest.builder()
                .couponCode("SAVE20")
                .cartId("1")
                .build();
        
        result = CouponValidationResult.builder()
                .valid(true)
                .couponCode("SAVE20")
                .discountType("PERCENTAGE")
                .discountValue(20.0)
                .minCartValue(50.0)
                .expiryDate(LocalDate.now().plusDays(30))
                .message("Coupon is valid and applicable")
                .applicableItems(new ArrayList<>())
                .build();
    }
    
    @Test
    void testValidateCoupon_Success() throws Exception {
        when(couponService.validateCoupon(anyString(), anyString()))
                .thenReturn(result);
        
        mockMvc.perform(post("/coupon/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.couponCode").value("SAVE20"))
                .andExpect(jsonPath("$.message").value("Coupon is valid and applicable"));
        
        verify(couponService, times(1)).validateCoupon(anyString(), anyString());
    }
    
    @Test
    void testValidateCoupon_InvalidRequest() throws Exception {
        ValidateCouponRequest invalidRequest = ValidateCouponRequest.builder()
                .couponCode("")
                .cartId("")
                .build();
        
        mockMvc.perform(post("/coupon/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        
        verify(couponService, never()).validateCoupon(anyString(), anyString());
    }
}

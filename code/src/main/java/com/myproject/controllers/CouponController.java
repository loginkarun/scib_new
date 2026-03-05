package com.myproject.controllers;

import com.myproject.models.dtos.CouponValidationResult;
import com.myproject.models.dtos.ValidateCouponRequest;
import com.myproject.services.CouponServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for coupon validation operations
 */
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
public class CouponController {
    
    private final CouponServiceInterface couponService;
    
    /**
     * Validate coupon code
     * POST /api/coupon/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResult> validateCoupon(
            @Valid @RequestBody ValidateCouponRequest request) {
        
        log.info("Received request to validate coupon: {}", request.getCouponCode());
        CouponValidationResult result = couponService.validateCoupon(
                request.getCouponCode(), 
                request.getCartId());
        return ResponseEntity.ok(result);
    }
}

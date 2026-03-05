package com.myproject.exceptions;

import com.myproject.models.dtos.ErrorDetail;
import com.myproject.models.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for all controllers
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartNotFoundException(CartNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("CART_NOT_FOUND")
                .message(ex.getMessage())
                .details(new ArrayList<>())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotFoundException(CouponNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("COUPON_NOT_FOUND")
                .message(ex.getMessage())
                .details(new ArrayList<>())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CouponExpiredException.class)
    public ResponseEntity<ErrorResponse> handleCouponExpiredException(CouponExpiredException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("COUPON_EXPIRED")
                .message(ex.getMessage())
                .details(new ArrayList<>())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(CouponNotApplicableException.class)
    public ResponseEntity<ErrorResponse> handleCouponNotApplicableException(CouponNotApplicableException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("COUPON_NOT_APPLICABLE")
                .message(ex.getMessage())
                .details(new ArrayList<>())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDetail> details = new ArrayList<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(ErrorDetail.builder()
                    .field(error.getField())
                    .issue(error.getDefaultMessage())
                    .build());
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed")
                .details(details)
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .traceId(UUID.randomUUID().toString())
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred")
                .details(new ArrayList<>())
                .build();
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

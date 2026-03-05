package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for error response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    private String traceId;
    private String errorCode;
    private String message;
    private List<ErrorDetail> details;
    
    public ErrorResponse(String errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.traceId = java.util.UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.message = message;
    }
}
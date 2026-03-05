package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for error detail.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    
    private String field;
    private String issue;
}
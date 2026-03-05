package com.myproject.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a coupon
 */
@Entity
@Table(name = "coupon")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    
    @Id
    @Column(name = "code", length = 50)
    private String code;
    
    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    
    @Column(name = "discount_value", nullable = false)
    private Double discountValue;
    
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;
    
    @Column(name = "min_cart_value")
    private Double minCartValue;
    
    @ElementCollection
    @CollectionTable(name = "coupon_applicable_item", joinColumns = @JoinColumn(name = "coupon_code"))
    @Column(name = "product_id")
    @Builder.Default
    private List<String> applicableItems = new ArrayList<>();
    
    public enum DiscountType {
        PERCENTAGE,
        FIXED_AMOUNT
    }
}

package com.myproject.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a shopping cart
 */
@Entity
@Table(name = "cart")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private String userId;
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();
    
    @Column(name = "total")
    private Double total;
    
    @Column(name = "original_total")
    private Double originalTotal;
    
    @Column(name = "applied_coupon_code")
    private String appliedCouponCode;
    
    @Column(name = "discount_amount")
    private Double discountAmount;
}

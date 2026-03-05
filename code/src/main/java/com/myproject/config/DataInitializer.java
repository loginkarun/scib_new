package com.myproject.config;

import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItemEntity;
import com.myproject.models.entities.Coupon;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes sample data for testing
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;
    
    @Override
    public void run(String... args) {
        log.info("Initializing sample data...");
        
        // Create sample coupons
        Coupon coupon1 = Coupon.builder()
                .code("SAVE20")
                .discountType(Coupon.DiscountType.PERCENTAGE)
                .discountValue(20.0)
                .expiryDate(LocalDate.now().plusDays(30))
                .minCartValue(50.0)
                .applicableItems(new ArrayList<>())
                .build();
        
        Coupon coupon2 = Coupon.builder()
                .code("FLAT10")
                .discountType(Coupon.DiscountType.FIXED_AMOUNT)
                .discountValue(10.0)
                .expiryDate(LocalDate.now().plusDays(60))
                .minCartValue(30.0)
                .applicableItems(new ArrayList<>())
                .build();
        
        Coupon coupon3 = Coupon.builder()
                .code("EXPIRED")
                .discountType(Coupon.DiscountType.PERCENTAGE)
                .discountValue(15.0)
                .expiryDate(LocalDate.now().minusDays(1))
                .minCartValue(25.0)
                .applicableItems(new ArrayList<>())
                .build();
        
        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);
        
        log.info("Created {} coupons", 3);
        
        // Create sample cart
        Cart cart = Cart.builder()
                .userId("user123")
                .items(new ArrayList<>())
                .total(0.0)
                .originalTotal(0.0)
                .discountAmount(0.0)
                .build();
        
        Cart savedCart = cartRepository.save(cart);
        
        // Create cart items
        CartItemEntity item1 = CartItemEntity.builder()
                .cart(savedCart)
                .productId("prod001")
                .quantity(2)
                .price(50.0)
                .build();
        
        CartItemEntity item2 = CartItemEntity.builder()
                .cart(savedCart)
                .productId("prod002")
                .quantity(1)
                .price(30.0)
                .build();
        
        List<CartItemEntity> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        
        savedCart.setItems(items);
        savedCart.setOriginalTotal(130.0);
        savedCart.setTotal(130.0);
        
        cartRepository.save(savedCart);
        
        log.info("Created sample cart with ID: {}", savedCart.getId());
        log.info("Sample data initialization complete!");
    }
}

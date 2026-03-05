package com.myproject.models.repositories;

import com.myproject.models.entities.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface for Coupon entity
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, String> {
    
    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND c.expiryDate >= :currentDate")
    Optional<Coupon> findValidCouponByCodeAndDate(@Param("code") String code, @Param("currentDate") LocalDate currentDate);
}

package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    // Custom method for finding a coupon by its description
    Coupon findByDescription(String description);
}

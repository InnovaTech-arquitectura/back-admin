package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // Custom method for finding a coupon by its description
    Coupon findByDescription(String description);

    /*@Query("SELECT u.email FROM Coupon c JOIN c.entrepreneurship e JOIN e.user u WHERE c.id = :couponId")
    Optional<String> findUserEmailByCouponId(@Param("couponId") Long couponId);*/
}

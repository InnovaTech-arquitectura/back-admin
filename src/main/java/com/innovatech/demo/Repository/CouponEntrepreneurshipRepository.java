package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.CouponEntrepreneurship;

@Repository
public interface CouponEntrepreneurshipRepository extends JpaRepository<CouponEntrepreneurship, Long> {
    
}

package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CouponService implements CrudService<Coupon, Long> {

    // Constructor injection to ensure proper dependency management
    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon findById(Long id) {
        // Correct: Long id is not autowired, it's passed as a method argument
        return couponRepository.findById(id).orElse(null);
    }

    public Page<Coupon> findAll(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        couponRepository.deleteById(id);
    }

    public Coupon findByDescription(String description) {
        return couponRepository.findByDescription(description);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public boolean existsById(Long id) {
        return couponRepository.existsById(id);
    }
}

package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Repository.CouponRepository;
//import com.innovatech.demo.Repository.EntrepreneurshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService implements CrudService<Coupon, Long> {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    //private EntrepreneurshipRepository entrepreneurshipRepository;

    @Override
    public Coupon findById(Long id) {
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
        // Guardar el cupón
        return couponRepository.save(coupon);
    }

    public boolean existsById(Long id) {
        return couponRepository.existsById(id);
    }
}

package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Entity.Functionality;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Repository.CouponRepository;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CouponService implements CrudService<Coupon, Long> {

    private final CouponRepository couponRepository;
    private final FunctionalityRepository functionalityRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository, FunctionalityRepository functionalityRepository) {
        this.couponRepository = couponRepository;
        this.functionalityRepository = functionalityRepository;
    }

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

    public boolean existsById(Long id) {
        return couponRepository.existsById(id);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    // Método para actualizar el cupón
    public Coupon updateCoupon(Long id, Coupon newCouponData) {
        Coupon existingCoupon = couponRepository.findById(id).orElse(null);
        if (existingCoupon == null) {
            throw new EntityNotFoundException("Coupon not found");
        }

        // Actualizamos los campos del cupón
        existingCoupon.setDescription(newCouponData.getDescription());
        existingCoupon.setDiscount(newCouponData.getDiscount());
        existingCoupon.setExpirationDate(newCouponData.getExpirationDate());
        existingCoupon.setExpirationPeriod(newCouponData.getExpirationPeriod());

        // Actualizamos las funcionalidades del cupón
        existingCoupon.getCouponFunctionalities().clear(); // Eliminamos las funcionalidades actuales
        for (Functionality functionality : newCouponData.getFunctionalities()) {
            existingCoupon.addFunctionality(functionality);
        }

        return couponRepository.save(existingCoupon); // Guardamos los cambios
    }
}

package com.innovatech.demo.Service;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Entity.CouponFunctionality;
import com.innovatech.demo.Entity.Functionality;
import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Repository.CouponRepository;
import com.innovatech.demo.Repository.FunctionalityRepository;
import com.innovatech.demo.Repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CouponService implements CrudService<Coupon, Long> {

    private final CouponRepository couponRepository;
    private final FunctionalityRepository functionalityRepository;
    private final PlanRepository planRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository, FunctionalityRepository functionalityRepository, PlanRepository planRepository) {
        this.couponRepository = couponRepository;
        this.functionalityRepository = functionalityRepository;
        this.planRepository = planRepository;
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
        Plan plan = coupon.getPlan();
        
        if (plan != null) {
            // Ensure CouponFunctionalities are properly associated
            if (coupon.getCouponFunctionalities() != null && !coupon.getCouponFunctionalities().isEmpty()) {
                for (CouponFunctionality couponFunctionality : coupon.getCouponFunctionalities()) {
                    Functionality functionality = couponFunctionality.getFunctionality();

                    // Check if functionality already exists
                    if (functionality.getId() != null) {
                        Functionality existingFunctionality = functionalityRepository.findById(functionality.getId()).orElse(null);
                        if (existingFunctionality != null) {
                            // Use existing functionality data
                            functionality = existingFunctionality;
                        }
                    }

                    // Save the functionality if it is new
                    if (functionality.getId() == null) {
                        functionality = functionalityRepository.save(functionality);
                    }

                    // Link the functionality to the coupon
                    couponFunctionality.setFunctionality(functionality);
                    couponFunctionality.setCoupon(coupon);
                }
            }

            // No need to add functionalities to the plan directly here, only manage through CouponFunctionality
        }

        // Save the coupon
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon newCouponData) {
        Coupon existingCoupon = couponRepository.findById(id).orElse(null);
        if (existingCoupon == null) {
            throw new RuntimeException("Coupon not found");
        }

        // Update coupon fields
        existingCoupon.setDescription(newCouponData.getDescription());
        existingCoupon.setExpirationDate(newCouponData.getExpirationDate());
        existingCoupon.setExpirationPeriod(newCouponData.getExpirationPeriod());

        // Update functionalities
        existingCoupon.getCouponFunctionalities().clear();
        for (CouponFunctionality couponFunctionality : newCouponData.getCouponFunctionalities()) {
            existingCoupon.addFunctionality(couponFunctionality.getFunctionality());
        }

        return couponRepository.save(existingCoupon);
    }
}

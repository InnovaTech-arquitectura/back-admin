package com.innovatech.demo.Controller;

import com.innovatech.demo.Entity.Coupon;
import com.innovatech.demo.Service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Obtener todos los cupones con paginación opcional
    @GetMapping("/all")
    public ResponseEntity<?> getAllCoupons(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int page) {
        try {
            Pageable pageable = PageRequest.of(page, limit);
            return ResponseEntity.ok(couponService.findAll(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // Obtener un cupón por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCouponById(@PathVariable Long id) {
        try {
            Coupon coupon = couponService.findById(id);

            if (coupon == null) {
                return new ResponseEntity<>("Coupon not found", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(coupon, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
    }

    // Agregar un nuevo cupón
    @PostMapping("/add")
    public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon) {
        try {
            // Verificar si ya existe el cupón para evitar duplicados
            Coupon existingCoupon = couponService.findByDescription(coupon.getDescription());
            if (existingCoupon != null) {
                return new ResponseEntity<>("Conflict: Duplicate entry", HttpStatus.CONFLICT);
            }

            // Guardar el nuevo cupón
            Coupon savedCoupon = couponService.save(coupon);

            return new ResponseEntity<>(savedCoupon, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to add coupon", HttpStatus.BAD_REQUEST);
        }
    }

    // Actualizar un cupón
    @PutMapping("/update")
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
        try {
            // Verificar si el cupón existe
            if (!couponService.existsById(coupon.getId())) {
                return new ResponseEntity<>("Conflict: Coupon not found", HttpStatus.NOT_FOUND);
            }

            couponService.save(coupon);

            return new ResponseEntity<>("Coupon updated successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to update coupon", HttpStatus.BAD_REQUEST);
        }
    }

    // Eliminar un cupón
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long id) {
        try {
            if (!couponService.existsById(id)) {
                return new ResponseEntity<>("Coupon not found", HttpStatus.NOT_FOUND);
            }

            couponService.deleteById(id);

            return new ResponseEntity<>("Coupon deleted successfully", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Unable to delete coupon", HttpStatus.BAD_REQUEST);
        }
    }
}

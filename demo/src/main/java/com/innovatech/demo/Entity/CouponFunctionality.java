package com.innovatech.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Coupon_Functionality")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponFunctionality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_coupon", nullable = false)
    @JsonBackReference // Prevents infinite recursion on the other side
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "id_functionality", nullable = false)
    private Functionality functionality;

    public CouponFunctionality(Coupon coupon, Functionality functionality) {
        this.coupon = coupon;
        this.functionality = functionality;
    }
}

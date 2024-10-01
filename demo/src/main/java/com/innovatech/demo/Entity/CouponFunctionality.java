package com.innovatech.demo.Entity;

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

    // Relación con Coupon
    @ManyToOne
    @JoinColumn(name = "id_coupon", nullable = false)
    private Coupon coupon;

    // Relación con Plan
    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    // Relación con Functionality
    @ManyToOne
    @JoinColumn(name = "id_functionality", nullable = false)
    private Functionality functionality;
}

package com.innovatech.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Coupon_entrepreneurship")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponEntrepreneurship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Entrepreneurship
    @ManyToOne
    @JoinColumn(name = "id_entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    // Relación con Coupon
    @ManyToOne
    @JoinColumn(name = "id_coupon", nullable = false)
    private Coupon coupon;

    // Campo de estado activo
    @Column(name = "active")
    @Builder.Default
    private Boolean active = true; // Valor por defecto asignado desde la aplicación

}

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

    @ManyToOne
    @JoinColumn(name = "id_Entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    @ManyToOne
    @JoinColumn(name = "id_Coupon", nullable = false)
    private Coupon coupon;

    @Column(name = "active")
    private Boolean active;

}

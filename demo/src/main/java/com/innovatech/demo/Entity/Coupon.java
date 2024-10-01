package com.innovatech.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "expiration_period")
    private Integer expirationPeriod;

    // Relación con Entrepreneur
    @ManyToOne
    @JoinColumn(name = "id_Entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    // Relación con la tabla intermedia CouponFunctionality
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponFunctionality> couponFunctionalities;
}

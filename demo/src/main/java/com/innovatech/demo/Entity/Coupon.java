package com.innovatech.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    // Many-to-One relationship with Entrepreneurship
    @ManyToOne
    @JoinColumn(name = "id_entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;


    // One-to-Many relationship with CouponFunctionality
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponFunctionality> couponFunctionalities = new ArrayList<>();

    // Method to add a functionality to the coupon
    public void addFunctionality(Functionality functionality) {
        CouponFunctionality couponFunctionality = new CouponFunctionality(this, functionality);
        this.couponFunctionalities.add(couponFunctionality);
    }

    // Method to get the functionalities of the coupon
    @Transient
    public List<Functionality> getFunctionalities() {
        List<Functionality> functionalities = new ArrayList<>();
        for (CouponFunctionality cf : couponFunctionalities) {
            functionalities.add(cf.getFunctionality());
        }
        return functionalities;
    }

    // Method to set the functionalities of the coupon
    public void setFunctionalities(List<Functionality> functionalities) {
        this.couponFunctionalities.clear();
        for (Functionality functionality : functionalities) {
            addFunctionality(functionality); // Use method to add functionality
        }
    }
}

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

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "expiration_period")
    private Integer expirationPeriod;

    // Relación Many-to-One con Entrepreneurship
    @ManyToOne
    @JoinColumn(name = "id_entrepreneurship", nullable = false)
    private Entrepreneurship entrepreneurship;

    // Relación Many-to-One con Plan
    @ManyToOne
    @JoinColumn(name = "id_plan", nullable = true)
    private Plan plan;

    // Relación One-to-Many con CouponFunctionality
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CouponFunctionality> couponFunctionalities = new ArrayList<>();  // Ensure it's initialized

    // Método para agregar una funcionalidad al cupón
    public void addFunctionality(Functionality functionality) {
        // Crear la relación con la funcionalidad del cupón
        CouponFunctionality couponFunctionality = new CouponFunctionality(this, functionality);
        this.couponFunctionalities.add(couponFunctionality);
        // **Note**: No need to add the functionality directly to the plan here
    }

    // Método para obtener las funcionalidades del cupón
    @Transient
    public List<Functionality> getFunctionalities() {
        List<Functionality> functionalities = new ArrayList<>();
        for (CouponFunctionality cf : couponFunctionalities) {
            functionalities.add(cf.getFunctionality());
        }
        return functionalities;
    }

    // Método para establecer las funcionalidades del cupón
    public void setFunctionalities(List<Functionality> functionalities) {
        this.couponFunctionalities.clear();
        for (Functionality functionality : functionalities) {
            addFunctionality(functionality); // Usar método para agregar funcionalidad y reflejar en el plan
        }
    }

    public List<CouponFunctionality> getCouponFunctionalities() {
        if (couponFunctionalities == null) {
            couponFunctionalities = new ArrayList<>();
        }
        return couponFunctionalities;
    }
    
    public void setCouponFunctionalities(List<CouponFunctionality> couponFunctionalities) {
        if (couponFunctionalities == null) {
            couponFunctionalities = new ArrayList<>();
        }
        this.couponFunctionalities = couponFunctionalities;
    }
}

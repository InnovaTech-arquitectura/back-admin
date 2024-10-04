package com.innovatech.demo.Entity;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "Subscription")
@Data
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "initial_date")
    private Date initialDate;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plan", nullable = false)
    private Plan plan;

    // The one-to-many relationship is not yet established, the entrepreneurship
    // entity must be created
    @ManyToOne
    @JoinColumn(name = "entrepreneurship_id")
    private Entrepreneurship entrepreneurship;

}
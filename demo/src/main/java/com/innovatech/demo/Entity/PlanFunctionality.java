package com.innovatech.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "Plan_Functionality")
@Data
@AllArgsConstructor
public class PlanFunctionality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with table Plan
    @ManyToOne
    private Plan plan;

    // Relationship with table Functionality
    @ManyToOne
    private Functionality functionality;
}
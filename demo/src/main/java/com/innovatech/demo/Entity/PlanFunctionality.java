package com.innovatech.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Plan_Functionality")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanFunctionality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with table Plan
    @ManyToOne
    @JoinColumn(name = "plan_id")
    @JsonIgnore
    private Plan plan;

    // Relationship with table Functionality
    @ManyToOne
    @JoinColumn(name = "functionality_id")
    private Functionality functionality;

    // Constructor
    public PlanFunctionality(Plan plan, Functionality functionality) {
        this.plan = plan;
        this.functionality = functionality;
    }
}
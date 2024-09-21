package com.innovatech.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Double price;

    // Relationship with table Subscription
    @JsonIgnore
    @OneToMany(mappedBy = "plan")
    private List<Subscription> subscriptions = new ArrayList<>();

    // Relationship with table PlanFunctionality
    @JsonIgnore
    @OneToMany(mappedBy = "plan")
    private List<PlanFunctionality> planFunctionality = new ArrayList<>();
}

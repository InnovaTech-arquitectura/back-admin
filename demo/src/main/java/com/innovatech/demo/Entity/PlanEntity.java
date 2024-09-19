package com.innovatech.demo.Entity;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "plan_entity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlanEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Double price;

    @JsonIgnore
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlanEntity> subscriptions= new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlanEntity> planFunctionality= new ArrayList<>();
    
    //@Column
    //private Long id_plan_payment;

}


package com.innovatech.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Plan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Builder.Default
    private List<Subscription> subscriptions = new ArrayList<>();

    // Relationship with table PlanFunctionality
    @JsonIgnore
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlanFunctionality> planFunctionality = new ArrayList<>();

    // Method to add functionalities to the plan
    public void addFunctionality(Functionality functionality) {
        PlanFunctionality planFunctionality = new PlanFunctionality(this, functionality);
        this.planFunctionality.add(planFunctionality);
    }

    // Method to get the functionalities of the plan
    @Transient
    public List<Functionality> getFunctionalities() {
        List<Functionality> functionalities = new ArrayList<>();
        for (PlanFunctionality pf : planFunctionality) {
            functionalities.add(pf.getFunctionality());
        }
        return functionalities;
    }

    // Method to set the functionalities of the plan
    public void setFunctionalities(List<Functionality> functionalities) {
        this.planFunctionality.clear();
        for (Functionality functionality : functionalities) {
            addFunctionality(functionality); // Use method to add functionality
        }
    }

}

package com.innovatech.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Functionality")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Functionality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    // Relationship with table PlanFunctionality
    @JsonIgnore
    @OneToMany(mappedBy = "functionality")
    @Builder.Default
    private List<PlanFunctionality> planFunctionality = new ArrayList<>();

}

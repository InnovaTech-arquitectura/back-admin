package com.innovatech.demo.Entity;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "administrative_employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "administrativeEmployee")
    @JsonIgnore
    @Builder.Default
    private List<AdministrativeEmployee> administrativeEmployees = new ArrayList<>();
} 


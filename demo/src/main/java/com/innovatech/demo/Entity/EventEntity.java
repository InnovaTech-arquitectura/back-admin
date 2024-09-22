package com.innovatech.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int Total_Cost;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private int Earnings;

    @Column(nullable = false)
    private int CostoLocal;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String modality;

    @Column(nullable = true)
    private Integer Quota;

    @JsonIgnore
    @OneToMany(mappedBy = "eventEntity")
    private List<Entrepreneurshipeventregistry> entrepreneurshipeventregistry;
}
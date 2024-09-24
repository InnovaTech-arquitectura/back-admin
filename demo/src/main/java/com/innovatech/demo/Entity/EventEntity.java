package com.innovatech.demo.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
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
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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
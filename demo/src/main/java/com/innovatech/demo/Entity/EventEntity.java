package com.innovatech.demo.Entity;

import java.util.List;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonPropertyOrder({
    "id",
    "name",
    "date",
    "date2",
    "totalCost",
    "earnings",
    "costoLocal",
    "place",
    "modality",
    "quota",
    "description",  
    "entrepreneurshipeventregistry"
})
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    Timestamp date;

    @Column(nullable = false)
    Timestamp date2;

    @Column(nullable = false)
    private int totalCost;

    @Column(nullable = false)
    private int earnings;

    @Column(nullable = false)
    private int costoLocal;

    @Column(nullable = false)
    private int place;

    @Column(nullable = false)
    private String modality;

    @Column(nullable = true)
    private Integer quota;

    @Column(nullable = true)
    private String description; 

    @OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Entrepreneurshipeventregistry> entrepreneurshipeventregistry;


    // Method to set the entrepreneurships of the event
    public void setEntrepreneurships(List<Entrepreneurship> entrepreneurships) {
        this.entrepreneurshipeventregistry.clear();
        for (Entrepreneurship entrepreneurship : entrepreneurships) {
            registryEntrepreneurship(entrepreneurship); // registry functionality
        }
    }

    // Method to registry the entrepreneurships to events
    public void registryEntrepreneurship(Entrepreneurship entrepreneurship) {
        Entrepreneurshipeventregistry entrepreneurshipeventregistry = new Entrepreneurshipeventregistry(this, entrepreneurship);
        this.entrepreneurshipeventregistry.add(entrepreneurshipeventregistry);
    }

     //constructor
     public EventEntity(Long id, String name, int totalCost, Timestamp date, Timestamp date2, int earnings, int costoLocal, int place, String modality, Integer quota, String description) {
        this.id = id;
        this.name = name;
        this.totalCost = totalCost;
        this.date = date;
        this.date2 = date2;
        this.earnings = earnings;
        this.costoLocal = costoLocal;
        this.place = place;
        this.modality = modality;
        this.quota = quota;
        this.description = description;
    }
}

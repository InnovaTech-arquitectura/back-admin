package com.innovatech.demo.Entity;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int totalCost;

    @Column(nullable = false)
    private String date;
    
    @Column(nullable = false)
    private String date2;

    @Column(nullable = false)
    private int earnings;

    @Column(nullable = false)
    private int costoLocal;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String modality;

    @Column(nullable = true)
    private Integer quota;

    @Column(nullable = true)
    private String Description;

    @OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Entrepreneurshipeventregistry> entrepreneurshipeventregistry;



    // Method to get the entrepreneurships of the event
    @Transient
    public List<Entrepreneurship> getEntrepreneurships() {
        List<Entrepreneurship> entrepreneurships = new ArrayList<>();
        for (Entrepreneurshipeventregistry registry : this.entrepreneurshipeventregistry) {
            entrepreneurships.add(registry.getEntrepreneurship());
        }
        return entrepreneurships;
    }

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
    public EventEntity(Long id, String name, int totalCost, String date, int earnings, int costoLocal, String place, String modality, Integer quota, String description) {
        this.id = id;
        this.name = name;
        this.totalCost = totalCost;
        this.date = date;
        this.earnings = earnings;
        this.costoLocal = costoLocal;
        this.place = place;
        this.modality = modality;
        this.quota = quota;
        this.Description = description;
    }
    
}
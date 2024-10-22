package com.innovatech.demo.Entity;

import java.util.List;

import org.hibernate.mapping.Set;

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
    private int earnings;

    @Column(nullable = false)
    private int costoLocal;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private String modality;

    @Column(nullable = true)
    private Integer quota;

    @JsonIgnore
    @OneToMany(mappedBy = "eventEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Entrepreneurshipeventregistry> entrepreneurshipeventregistry;

    // Método para agregar inscripciones de emprendedores
    public void addEntrepreneurshipEventRegistry(Entrepreneurshipeventregistry registry) {
        this.entrepreneurshipeventregistry.add(registry);
        registry.setEventEntity(this); // Asegura la bidireccionalidad
    }
}
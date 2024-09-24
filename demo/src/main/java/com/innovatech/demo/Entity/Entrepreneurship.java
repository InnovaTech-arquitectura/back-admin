package com.innovatech.demo.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "entrepreneurship_entity")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Entrepreneurship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique=true, nullable = false)
    String name;

    String logo;

    String description; 

    String names;

    String lastnames;

    @ManyToAny(fetch = FetchType.EAGER)
    @JoinTable(
        name = "course_entrepreneurship",
        joinColumns = @JoinColumn(name = "entrepreneurship_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();


    public Entrepreneurship(String name, String logo, String description, String names, String lastnames) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.names = names;
        this.lastnames = lastnames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrepreneurship)) return false;
        Entrepreneurship that = (Entrepreneurship) o;
        return id != null && id.equals(that.id); // Compara por id
    }

    @Override
    public int hashCode() {
        return 31; // O simplemente puede devolver id.hashCode() si id no es null
    }


}

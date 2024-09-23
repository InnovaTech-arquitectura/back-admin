package com.innovatech.demo.Entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovatech.demo.Entity.Enum.Modality;

@Entity
@Table(name = "Course")
@Data
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String link;

    @Column(nullable = false)
    String description;

    Float score;

    @Column(nullable = false)
    Timestamp date;

    @Column(unique = true, nullable = false)
    String title;

    @Column(nullable = false)
    Integer places;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) CHECK (modality IN ('virtual', 'presencial'))")
    @Enumerated(EnumType.STRING)
    Modality modality;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Set<Entrepreneurship> entrepreneurships = new HashSet<>();

    // Constructor con todos los atributos excepto 'id'
    public Course(String link, String description, Float score, Timestamp date, String title, int places,
            Modality modality) {
        this.link = link;
        this.description = description;
        this.score = score;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
    }

    public Course(long id, String link, String description, Float score, Timestamp date, String title, int places,
            Modality modality) {
        this.link = link;
        this.description = description;
        this.score = score;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
    }

    public Course(String link, String description, Float score, Timestamp date, String title, int places,
            Modality modality, Set<Entrepreneurship> entrepreneurList) {
        this.link = link;
        this.description = description;
        this.score = score;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
        this.entrepreneurships = entrepreneurList != null ? entrepreneurList : new HashSet<>();
    }

    public void addEntrepreneurship(Entrepreneurship entrepreneurship) {
        this.entrepreneurships.add(entrepreneurship);
        entrepreneurship.getCourses().add(this); // Aseguramos la bidireccionalidad
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Course))
            return false;
        Course that = (Course) o;
        return id != null && id.equals(that.id); // Compara por id
    }

    @Override
    public int hashCode() {
        return 31; // O simplemente puede devolver id.hashCode() si id no es null
    }

}

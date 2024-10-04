package com.innovatech.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CourseEntrepreneurship")
@Data
@Getter
@Setter
@NoArgsConstructor
public class CourseEntrepreneurship {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer puntaje=0;

    @ManyToOne
    @JsonIgnore
    private Entrepreneurship  entrepreneurship;

    @ManyToOne
    @JsonIgnore
    private Course  course;

    public CourseEntrepreneurship(Entrepreneurship entrepreneurship, Course course) {
        this.puntaje = 0;
        this.entrepreneurship = entrepreneurship;
        this.course = course;
    }
}

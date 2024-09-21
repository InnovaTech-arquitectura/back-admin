package com.innovatech.demo.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

    @ManyToMany (mappedBy = "entrepreneurList",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courseList = new ArrayList<>();

    public Entrepreneurship(String name, String logo, String description, String names, String lastnames) {
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.names = names;
        this.lastnames = lastnames;
    }

    public void addCourse(Course course) {
        this.courseList.add(course);
        course.getEntrepreneurList().add(this); // Aseguramos la bidireccionalidad
    }
}

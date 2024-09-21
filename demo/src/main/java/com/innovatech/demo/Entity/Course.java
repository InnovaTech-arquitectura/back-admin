package com.innovatech.demo.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "course")
@Data
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    String description;

    Float score;

    @Column(nullable = false)
    Date date;

    @Column(unique = true, nullable = false)
    String title;

    @Column(nullable = false)
    int places;

    @Column(nullable = false,columnDefinition = "VARCHAR(255) CHECK (rol IN ('virtual', 'presencial'))")
    String modality;

}

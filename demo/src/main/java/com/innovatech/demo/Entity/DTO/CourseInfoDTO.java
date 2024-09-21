package com.innovatech.demo.Entity.DTO;

import java.sql.Date;
import java.sql.Timestamp;

import com.innovatech.demo.Entity.Enum.Modality;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CourseInfoDTO {
    
    String id;

    String link;

    String description;

    Timestamp date;

    String title;

    Integer places;

    Modality modality;

    int availablePlaces;

    float score;

    public CourseInfoDTO(String id, 
    String link, 
    String description,
    Float score, 
    Timestamp date, 
    String title, 
    Integer places, 
    Modality modality) {
        this.id = id;
        this.link = link;
        this.description = description;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
        this.score=score;
    }
}

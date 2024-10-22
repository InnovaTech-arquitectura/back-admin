package com.innovatech.demo.DTO;

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
    
    Long id;

    String link;

    String description;

    Timestamp date;

    String title;

    Integer places;

    Modality modality;

    int availablePlaces;

    float score;

    public CourseInfoDTO(Long id, 
    String link, 
    String description,
    Float score, 
    Timestamp date, 
    String title, 
    Integer places, 
    Modality modality,
    int availablePlaces) {
        this.id = id;
        this.link = link;
        this.description = description;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
        this.score=score;
        this.availablePlaces=availablePlaces;
    }
}

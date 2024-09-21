package com.innovatech.demo.Entity.DTO;

import java.sql.Timestamp;

import com.innovatech.demo.Entity.Enum.Modality;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
public class CourseDTONoID {

    String link;

    String description;

    Timestamp date;

    String title;

    Integer places;

    Modality modality;

    float score;

    public CourseDTONoID( String link, 
    String description, 
    Float score, 
    Timestamp date, 
    String title, 
    Integer places,
    Modality modality) {
        this.link = link;
        this.description = description;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
        this.score=score;
    }
}

package com.innovatech.demo.DTO;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseDTO {

    String id;

    String link;

    String description;

    Date date;

    String title;

    int places;

    String modality;

    public CourseDTO(String id, String link, String description, Float score, Date date, String title, int places,
            String modality) {
        this.id = id;
        this.link = link;
        this.description = description;
        this.date = date;
        this.title = title;
        this.places = places;
        this.modality = modality;
    }
}

package com.innovatech.demo.Entity;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Subscription")
@Data
@AllArgsConstructor
@Builder
public class PlanFunctionality {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plan", nullable = false)
    private PlanEntity plan;

    // The one-to-many relation is not yet established, the entrepreneurships entity must be created
    //@ManyToOne
    //@JoinColumn(name = "id_entrepreneurship", nullable = false)
    //private Entrepreneurship entrepreneurship;
}
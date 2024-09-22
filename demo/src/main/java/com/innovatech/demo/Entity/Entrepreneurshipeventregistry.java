package com.innovatech.demo.Entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EntrepreneurshipEventRegistry")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entrepreneurshipeventregistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne
    //@JoinColumn(name = "Id_Entrepreneurship", referencedColumnName = "id")
    //private Entrepreneurship entrepreneurship;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_event", nullable = false)
    private EventEntity eventEntity; 

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(nullable = false)
    private double amountPaid;
}

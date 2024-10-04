package com.innovatech.demo.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne
    @JoinColumn(name = "Id_Entrepreneurship", referencedColumnName = "id")
    @JoinColumn(name = "name_Entrepreneurship", referencedColumnName = "name")
    private Entrepreneurship entrepreneurship;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private EventEntity eventEntity;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(nullable = false)
    private double amountPaid;

    public Entrepreneurshipeventregistry(EventEntity eventEntity, Entrepreneurship entrepreneurship) {
        this.eventEntity = eventEntity;
        this.entrepreneurship = entrepreneurship;
    }

}

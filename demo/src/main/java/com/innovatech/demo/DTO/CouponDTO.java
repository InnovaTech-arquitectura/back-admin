package com.innovatech.demo.DTO;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CouponDTO {

    private Long id;
    private String description;
    private Date expirationDate;
    private Integer expirationPeriod;
    private Long entrepreneurshipId;
    private Long planId; // Se agregará un plan
    private List<Long> functionalityIds; // Lista de IDs de funcionalidades a asociar
}

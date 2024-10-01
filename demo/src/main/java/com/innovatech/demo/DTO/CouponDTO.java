package com.innovatech.demo.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class CouponDTO {

    private Long id;
    private String description;
    private Double discount;
    private Date expirationDate;
    private Integer expirationPeriod;
    private Long entrepreneurshipId;
}

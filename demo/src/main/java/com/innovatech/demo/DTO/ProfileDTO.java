package com.innovatech.demo.DTO;

import lombok.Data;

@Data
public class ProfileDTO {
    private int idCard;
    private String name;
    private String email;
    private String password;
    private String role;
}

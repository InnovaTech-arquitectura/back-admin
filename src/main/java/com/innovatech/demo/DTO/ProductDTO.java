package com.innovatech.demo.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    
    private String name;
    private int quantity;
    private double price;
    private double cost;
    private String description;
    
    // Para la imagen del producto (subida desde el cliente)
    private MultipartFile picture;  
    private Long IdUser_Entity;
}

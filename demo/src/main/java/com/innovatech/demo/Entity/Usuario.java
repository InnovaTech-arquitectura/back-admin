package com.innovatech.demo.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_entity")
@Data

public class Usuario  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "nombre")
    private String nombre;

    private boolean enabled = true;

    // Constructor sin la propiedad "id"
    public Usuario(String nombre, String username, String password) {
        this.nombre = nombre;
        this.email = username;
        this.password = password;
    }

}

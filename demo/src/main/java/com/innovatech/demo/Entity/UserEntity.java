package com.innovatech.demo.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_entity")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int idCard;

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToOne()
    private AdministrativeEmployee administrativeEmployee;
}

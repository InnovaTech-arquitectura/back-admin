package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método para buscar por nombre de rol
    Optional<Role> findByName(String name);

    // Método para verificar existencia por nombre de rol
    boolean existsByName(String name);
}

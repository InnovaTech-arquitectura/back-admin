package com.innovatech.demo.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.innovatech.demo.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   
    // Buscar usuario por ID de tarjeta
    Optional<UserEntity> findByIdCard(int idCard);

    // Buscar usuario por email
    Optional<UserEntity> findByEmail(String email);

    // Verificar si existe usuario por ID de tarjeta
    boolean existsByIdCard(int idCard);

    // Verificar si existe usuario por email
    boolean existsByEmail(String email);

    // Contar usuarios por nombre de rol
    @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.role.name = :roleName")
    int countByRoleName(String roleName);
}

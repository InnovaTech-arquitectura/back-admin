package com.innovatech.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   Optional<UserEntity> findByIdCard(int idCard);

   Optional<UserEntity> findByEmail(String email);

   boolean existsByIdCard(int idCard);

   boolean existsByEmail(String email);

}

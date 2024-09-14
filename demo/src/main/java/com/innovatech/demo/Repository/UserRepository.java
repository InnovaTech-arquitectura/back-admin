package com.innovatech.demo.Repository;


import com.innovatech.demo.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   Optional<UserEntity> findByIdCard (int idCard);
   Optional<UserEntity> findByEmail (String email);
   boolean existsByIdCard (int idCard);
   boolean existsByEmail (String email);



}

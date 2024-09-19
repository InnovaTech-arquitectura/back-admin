package com.innovatech.demo.Repository;

import java.util.Optional;

import com.innovatech.demo.Entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    
    Optional<PlanEntity> findById (int id);
   
}

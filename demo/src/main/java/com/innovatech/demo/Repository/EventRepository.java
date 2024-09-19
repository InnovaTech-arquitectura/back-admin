package com.innovatech.demo.Repository;

import java.util.Optional;

import com.innovatech.demo.Entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>{
    
    Optional<EventRepository> findById (int id);
}

package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findById(Long id);
    
    EventEntity findByName(String name);
}
package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;

@Repository
public interface EntrepreneurshipeventregistryRepository extends JpaRepository<Entrepreneurshipeventregistry, Long>{
    
}

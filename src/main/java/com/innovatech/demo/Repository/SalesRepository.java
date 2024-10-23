package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Sales;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {
    
}

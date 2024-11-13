package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}

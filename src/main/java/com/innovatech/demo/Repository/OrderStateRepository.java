package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.demo.Entity.OrderState;

@Repository
public interface OrderStateRepository extends JpaRepository<OrderState, Long> {
    
}

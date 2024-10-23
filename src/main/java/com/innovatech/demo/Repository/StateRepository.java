package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.innovatech.demo.Entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    
}

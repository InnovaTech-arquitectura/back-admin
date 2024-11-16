package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
}

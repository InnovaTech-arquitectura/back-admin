package com.innovatech.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Functionality;

@Repository
public interface FunctionalityRepository extends JpaRepository<Functionality, Long> {

}

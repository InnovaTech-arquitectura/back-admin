package com.innovatech.demo.Repository;

import com.innovatech.demo.Entity.PlanFunctionality;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanFunctionalityRepository extends JpaRepository<PlanFunctionality, Long> {

}

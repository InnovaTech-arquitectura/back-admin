package com.innovatech.demo.Repository;

import java.util.Optional;

import com.innovatech.demo.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findById(int id);

}

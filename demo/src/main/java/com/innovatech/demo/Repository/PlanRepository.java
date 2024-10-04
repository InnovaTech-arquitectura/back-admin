package com.innovatech.demo.Repository;

import java.util.Optional;

import com.innovatech.demo.Entity.Plan;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findById(int id);

    Plan findByName(String name);

    @Query("SELECT s.plan.name, COUNT(s), SUM(s.amount) FROM Subscription s GROUP BY s.plan.name")
    List<Plan> Subscriptionforplan();

}

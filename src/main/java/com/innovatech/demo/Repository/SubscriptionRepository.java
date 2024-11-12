package com.innovatech.demo.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.Subscription;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByPlan(Plan plan);

    @Query("SELECT s.plan.id, s.plan.name, COUNT(s.id) FROM Subscription s " +
        "WHERE YEAR(s.initialDate) = :year " +
        "GROUP BY s.plan.id, s.plan.name")
    List<Object[]> countUsersByPlanForYear(@Param("year") int year);

    @Query("SELECT s.plan.id, s.plan.name, SUM(s.amount) FROM Subscription s " +
        "WHERE YEAR(s.initialDate) = :year " +
        "GROUP BY s.plan.id, s.plan.name")
    List<Object[]> sumIncomeByPlanForYear(@Param("year") int year);

    boolean existsByPlanIdAndExpirationDateAfter(Long planId, Date currentDate);

}

package com.innovatech.demo.Repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;

import com.innovatech.demo.Entity.Plan;
import com.innovatech.demo.Entity.Subscription;


public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByPlan(Plan plan);
}

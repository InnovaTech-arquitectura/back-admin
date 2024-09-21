package com.innovatech.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Course;

@Repository
public interface RepositoryCourse extends JpaRepository<Course, Long>{
    
    @Query("SELECT c FROM Course c WHERE c.date > CURRENT_TIMESTAMP")
    List<Course> findAllActive();
}

package com.innovatech.demo.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.innovatech.demo.Entity.CourseEntrepreneurship;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CourseEntrepreneurshipRepository extends JpaRepository<CourseEntrepreneurship, Long> {
	
}

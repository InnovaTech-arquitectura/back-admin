package com.innovatech.demo.Repository;

import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface RepositoryEntrepreneurship extends JpaRepository<Entrepreneurship, Long> {
    
}

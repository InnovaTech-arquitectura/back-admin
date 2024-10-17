package com.innovatech.demo.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurship;

@Repository
public interface EntrepreneurshipRepository extends JpaRepository<Entrepreneurship, Long> {

    Entrepreneurship findByName(String name);

    Optional<Entrepreneurship> findById(int id);

}
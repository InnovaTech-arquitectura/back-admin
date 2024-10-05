package com.innovatech.demo.Repository;

import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurship;
import com.innovatech.demo.Entity.Entrepreneurshipeventregistry;
import com.innovatech.demo.Entity.EventEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RepositoryEntrepreneurship extends JpaRepository<Entrepreneurship, Long> {

    @Query("SELECT e FROM Entrepreneurshipeventregistry e WHERE e.eventEntity = :eventEntity AND e.entrepreneurship = :entrepreneurship")
Entrepreneurshipeventregistry findByEventAndEntrepreneurship(@Param("eventEntity") EventEntity eventEntity, @Param("entrepreneurship") Entrepreneurship entrepreneurship);

}

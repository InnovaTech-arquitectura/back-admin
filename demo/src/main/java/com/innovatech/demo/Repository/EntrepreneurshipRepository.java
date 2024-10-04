package com.innovatech.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurship;

@Repository
public interface EntrepreneurshipRepository extends JpaRepository<Entrepreneurship, Long> {

    Entrepreneurship findByName(String name);

    Optional<Entrepreneurship> findById(int id);

    @Query("SELECT e FROM Entrepreneurship e")
    List<Entrepreneurship> findAllEntrepreneurships();

    @Query("SELECT e.id, SUM(s.amount) + SUM(r.amountPaid) FROM Subscription s LEFT JOIN Entrepreneurship e ON s.entrepreneurship.id = e.id LEFT JOIN Entrepreneurshipeventregistry r ON e.id = r.entrepreneurship.id GROUP BY e.id")
    List<Entrepreneurship> IngresosTotalesEntrepreneurships();


}
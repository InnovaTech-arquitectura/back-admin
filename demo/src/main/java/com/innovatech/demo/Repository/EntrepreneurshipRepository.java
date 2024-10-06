package com.innovatech.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.Entrepreneurship;

@Repository
public interface EntrepreneurshipRepository extends JpaRepository<Entrepreneurship, Long> {

    Entrepreneurship findByName(String name);

    Optional<Entrepreneurship> findById(int id);

    @Query("SELECT e FROM Entrepreneurship e")
    List<Entrepreneurship> findAllEntrepreneurships();

    @Query("SELECT e, p.name FROM Entrepreneurship e " +
        "JOIN Subscription s ON e.id = s.entrepreneurship.id " +
        "JOIN Plan p ON s.plan.id = p.id " +
        "WHERE CURRENT_DATE BETWEEN s.initialDate AND s.expirationDate " +
        "AND s.initialDate = (SELECT MAX(s2.initialDate) FROM Subscription s2 WHERE s2.entrepreneurship.id = e.id)")
        List<Object[]> findAllWithActivePlan();


    @Query("SELECT e.id, e.name, SUM(COALESCE(s.amount, 0)) + SUM(COALESCE(r.amountPaid, 0)) AS totalIncome " +
            "FROM Entrepreneurship e " +
            "LEFT JOIN e.subscriptions s " +
            "LEFT JOIN e.eventRegistries r " +
            "GROUP BY e.id, e.name")
    List<Object[]> IngresosTotalesEntrepreneurships();

    @Query("SELECT MONTH(s.initialDate) AS month, " +
            "SUM(COALESCE(s.amount, 0)) + SUM(COALESCE(r.amountPaid, 0)) AS totalIncome " +
            "FROM Entrepreneurship e " +
            "LEFT JOIN e.subscriptions s " +
            "LEFT JOIN e.eventRegistries r " +
            "WHERE (YEAR(s.initialDate) = :year OR YEAR(r.date) = :year) " +
            "AND (MONTH(s.initialDate) = :month OR MONTH(r.date) = :month) " +
            "GROUP BY MONTH(s.initialDate) " +
            "ORDER BY month")
    List<Object[]> getIncomeByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT MONTH(s.initialDate) AS month, " +
            "SUM(COALESCE(s.amount, 0)) + SUM(COALESCE(r.amountPaid, 0)) AS totalIncome " +
            "FROM Entrepreneurship e " +
            "LEFT JOIN e.subscriptions s " +
            "LEFT JOIN e.eventRegistries r " +
            "WHERE YEAR(s.initialDate) = :year OR YEAR(r.date) = :year " +
            "GROUP BY MONTH(s.initialDate) " +
            "ORDER BY month")
    List<Object[]> getAnnualIncomeByMonth(@Param("year") int year);

}
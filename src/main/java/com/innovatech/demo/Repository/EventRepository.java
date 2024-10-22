package com.innovatech.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.innovatech.demo.Entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findById(Long id);

    EventEntity findByName(String name);

    /* 
    @Query("SELECT MONTH(e.date) AS month, " +
        "SUM(COALESCE(e.totalCost, 0)) AS totalExpense " +
        "FROM Event e " +
        "WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month " +
        "GROUP BY MONTH(e.date)")
    List<Object[]> getExpensesByMonthAndYear(@Param("month") int month, @Param("year") int year);
    */

    @Query("SELECT YEAR(e.date) AS year, SUM(COALESCE(e.totalCost, 0)) AS totalExpense " +
       "FROM EventEntity e " +
       "WHERE YEAR(e.date) = :year " +
       "GROUP BY YEAR(e.date)")
    List<Object[]> getAnnualExpensesByYear(@Param("year") int year);


    

}
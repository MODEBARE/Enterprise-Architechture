package com.profitsystem.profit.repository;

import com.profitsystem.profit.entity.MonthlyProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyProfitRepository extends JpaRepository<MonthlyProfit, Long> {
    
    Optional<MonthlyProfit> findByMonth(YearMonth month);
    
    @Query("SELECT mp FROM MonthlyProfit mp WHERE YEAR(mp.month) = :year")
    List<MonthlyProfit> findByYear(@Param("year") int year);
    
    @Query("SELECT SUM(mp.profit) FROM MonthlyProfit mp WHERE YEAR(mp.month) = :year")
    BigDecimal getTotalProfitForYear(@Param("year") int year);
    
    @Query("SELECT mp FROM MonthlyProfit mp ORDER BY mp.month DESC")
    List<MonthlyProfit> findAllOrderByMonthDesc();
}
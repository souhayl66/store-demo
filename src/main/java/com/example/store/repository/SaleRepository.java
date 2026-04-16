package com.example.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.store.entity.Sale;   // <-- updated import

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s.product.name, SUM(s.quantity) " +
            "FROM Sale s GROUP BY s.product.name ORDER BY SUM(s.quantity) DESC")
    List<Object[]> findBestSellers();

    @Query("SELECT s.saleDate, SUM(s.totalPrice) " +
            "FROM Sale s GROUP BY s.saleDate ORDER BY s.saleDate ASC")
    List<Object[]> findDailyRevenue();

}

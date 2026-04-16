package com.example.store.repository;

import com.example.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Find products with stock less than a given threshold
    List<Product> findByStockLessThan(int threshold);
}

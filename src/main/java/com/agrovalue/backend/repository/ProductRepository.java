package com.agrovalue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agrovalue.backend.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByFarmerId(Long farmerId);
    long countByFarmerId(Long farmerId);
    long countByFarmerIdAndStockLessThan(Long farmerId, Integer stockThreshold);
}

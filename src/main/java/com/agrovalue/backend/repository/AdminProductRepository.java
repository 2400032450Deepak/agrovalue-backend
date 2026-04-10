package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p join fetch p.farmer")
    List<Product> findAllWithFarmer();
}

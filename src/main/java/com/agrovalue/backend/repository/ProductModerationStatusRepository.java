package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.ProductModerationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductModerationStatusRepository extends JpaRepository<ProductModerationStatus, Long> {

    @Query("select count(p) from ProductModerationStatus p where p.status = 'ACTIVE'")
    long countActiveProducts();
}

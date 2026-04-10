package com.agrovalue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agrovalue.backend.entity.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductId(Long productId);

    @Query("select avg(r.rating) from Review r where r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);

    @Query("select avg(r.rating) from Review r where r.product.farmer.id = :farmerId")
    Double findAverageRatingByFarmerId(@Param("farmerId") Long farmerId);
}

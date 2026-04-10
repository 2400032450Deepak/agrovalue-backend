package com.agrovalue.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.agrovalue.backend.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query("select distinct o from Order o join o.items oi join oi.product p where p.farmer.id = :farmerId")
    List<Order> findOrdersByFarmerId(@Param("farmerId") Long farmerId);

    @Query("select count(distinct o.id) from Order o join o.items oi join oi.product p where p.farmer.id = :farmerId")
    long countOrdersByFarmerId(@Param("farmerId") Long farmerId);
}

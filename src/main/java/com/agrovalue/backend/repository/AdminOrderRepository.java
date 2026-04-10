package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AdminOrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderByOrderedAtDesc();

    @Query("select coalesce(sum(o.totalAmount), 0) from Order o")
    BigDecimal totalRevenue();

    @Query("""
        select f.id, f.name, f.email,
               count(distinct p.id),
               count(distinct o.id),
               coalesce(sum(oi.price * oi.quantity), 0)
        from User f
        join f.roles r
        left join Product p on p.farmer.id = f.id
        left join OrderItem oi on oi.product.farmer.id = f.id
        left join oi.order o
        where r.name = 'ROLE_FARMER'
        group by f.id, f.name, f.email
        """)
    List<Object[]> farmerAnalytics();
}

package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminUserRepository extends JpaRepository<User, Long> {

    @Query("select count(distinct u.id) from User u join u.roles r where r.name = 'ROLE_FARMER'")
    long countFarmers();

    @Query("select count(distinct u.id) from User u join u.roles r where r.name = 'ROLE_BUYER'")
    long countBuyers();

    @Query("select distinct u from User u left join fetch u.roles")
    List<User> findAllWithRoles();
}

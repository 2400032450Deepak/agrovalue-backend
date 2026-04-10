package com.agrovalue.backend.repository;

import com.agrovalue.backend.entity.UserAccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountStatusRepository extends JpaRepository<UserAccountStatus, Long> {
}

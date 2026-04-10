package com.agrovalue.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_account_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountStatus {

    @Id
    private Long userId;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

package com.agrovalue.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_moderation_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModerationStatus {

    @Id
    private Long productId;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}

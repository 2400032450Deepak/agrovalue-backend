package com.agrovalue.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

// 🔥 ADD THIS
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 FIX 1
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    // 🔥 FIX 2
    @ManyToMany
    @JoinTable(
        name = "cart_products",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore
    private List<Product> products;
}
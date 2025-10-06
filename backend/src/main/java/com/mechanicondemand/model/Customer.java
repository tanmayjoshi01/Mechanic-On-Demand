package com.mechanicondemand.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Customer Entity - Represents a customer in the system
 * This maps to the 'customers' table in the database
 */
@Entity
@Table(name = "customers")
@Data  // Lombok: Automatically generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: Generates no-argument constructor
@AllArgsConstructor  // Lombok: Generates constructor with all arguments
public class Customer {
    
    @Id  // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;  // Will be encrypted using BCrypt
    
    @Column(nullable = false)
    private String phone;
    
    private String address;
    
    // GPS coordinates for finding nearby mechanics
    private Double latitude;
    private Double longitude;
    
    @Column(nullable = false)
    private String role = "CUSTOMER";  // Role for authorization
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

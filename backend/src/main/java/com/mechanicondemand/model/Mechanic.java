package com.mechanicondemand.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Mechanic Entity - Represents a mechanic in the system
 * This maps to the 'mechanics' table in the database
 */
@Entity
@Table(name = "mechanics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mechanic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String phone;
    
    private String specialty;  // e.g., "Car", "Bike", "Truck", "All"
    
    private String address;
    
    // GPS coordinates for location-based search
    private Double latitude;
    private Double longitude;
    
    // Availability status
    @Column(nullable = false)
    private Boolean available = true;
    
    // Rating system
    private Double rating = 0.0;
    
    @Column(name = "total_jobs")
    private Integer totalJobs = 0;
    
    @Column(nullable = false)
    private String role = "MECHANIC";
    
    // Pricing
    @Column(name = "hourly_rate")
    private Double hourlyRate = 50.0;
    
    @Column(name = "monthly_subscription")
    private Double monthlySubscription = 999.0;
    
    @Column(name = "yearly_subscription")
    private Double yearlySubscription = 9999.0;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

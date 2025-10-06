package com.mechanicondemand.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Booking Entity - Represents a service booking
 * Links customers with mechanics
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Many bookings can belong to one customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    // Many bookings can belong to one mechanic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;
    
    @Column(nullable = false)
    private String vehicleType;  // "Car", "Bike", "Truck", etc.
    
    @Column(nullable = false)
    private String vehicleModel;
    
    @Column(nullable = false)
    private String problemDescription;
    
    private String location;
    
    private Double latitude;
    private Double longitude;
    
    // Booking status: PENDING, ACCEPTED, REJECTED, IN_PROGRESS, COMPLETED, CANCELLED
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    
    // Subscription type: HOURLY, MONTHLY, YEARLY
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    @Column(name = "completed_time")
    private LocalDateTime completedTime;
    
    // Rating after service completion (1-5 stars)
    private Integer rating;
    
    private String feedback;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

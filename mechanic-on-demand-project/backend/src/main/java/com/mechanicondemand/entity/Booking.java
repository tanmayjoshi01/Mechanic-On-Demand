package com.mechanicondemand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Booking Entity
 *
 * Represents a service booking between a customer and a mechanic.
 * Contains all booking details including status, pricing, and location information.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "mechanic_id", nullable = false)
    private Mechanic mechanic;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    @Column(name = "scheduled_datetime", nullable = false)
    private LocalDateTime scheduledDateTime;

    @Column(name = "estimated_duration_hours")
    @DecimalMin(value = "0.1", message = "Estimated duration must be at least 0.1 hours")
    private BigDecimal estimatedDurationHours;

    @Column(name = "total_price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    private BigDecimal totalPrice;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "customer_latitude")
    private Double customerLatitude;

    @Column(name = "customer_longitude")
    private Double customerLongitude;

    @Column(name = "mechanic_notes")
    private String mechanicNotes;

    @Column(name = "customer_rating")
    private Integer customerRating; // 1-5 stars

    @Column(name = "mechanic_rating")
    private Integer mechanicRating; // 1-5 stars

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Booking() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Booking(Customer customer, Mechanic mechanic, Service service, LocalDateTime scheduledDateTime) {
        this();
        this.customer = customer;
        this.mechanic = mechanic;
        this.service = service;
        this.scheduledDateTime = scheduledDateTime;
    }

    // Lifecycle methods
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Mechanic getMechanic() {
        return mechanic;
    }

    public void setMechanic(Mechanic mechanic) {
        this.mechanic = mechanic;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
    }

    public BigDecimal getEstimatedDurationHours() {
        return estimatedDurationHours;
    }

    public void setEstimatedDurationHours(BigDecimal estimatedDurationHours) {
        this.estimatedDurationHours = estimatedDurationHours;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Double getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(Double customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public Double getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(Double customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public String getMechanicNotes() {
        return mechanicNotes;
    }

    public void setMechanicNotes(String mechanicNotes) {
        this.mechanicNotes = mechanicNotes;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getMechanicRating() {
        return mechanicRating;
    }

    public void setMechanicRating(Integer mechanicRating) {
        this.mechanicRating = mechanicRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean isCompleted() {
        return status == BookingStatus.COMPLETED;
    }

    public boolean isCancelled() {
        return status == BookingStatus.CANCELLED;
    }

    public boolean canBeRated() {
        return isCompleted() && (customerRating == null || mechanicRating == null);
    }
}
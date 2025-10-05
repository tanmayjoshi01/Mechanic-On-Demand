package com.mechanicondemand.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 💰 Pricing Entity
 * 
 * This class represents the pricing table in our database.
 * It stores different service types and their monthly/yearly pricing.
 */
@Entity
@Table(name = "pricing")
public class Pricing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Service type is required")
    @Column(name = "service_type", nullable = false, unique = true)
    private String serviceType;
    
    @NotNull(message = "Monthly price is required")
    @DecimalMin(value = "0.0", message = "Monthly price must be positive")
    @Column(name = "monthly_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal monthlyPrice;
    
    @NotNull(message = "Yearly price is required")
    @DecimalMin(value = "0.0", message = "Yearly price must be positive")
    @Column(name = "yearly_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal yearlyPrice;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Pricing() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Pricing(String serviceType, BigDecimal monthlyPrice, BigDecimal yearlyPrice, String description) {
        this();
        this.serviceType = serviceType;
        this.monthlyPrice = monthlyPrice;
        this.yearlyPrice = yearlyPrice;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    
    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }
    
    public BigDecimal getYearlyPrice() { return yearlyPrice; }
    public void setYearlyPrice(BigDecimal yearlyPrice) { this.yearlyPrice = yearlyPrice; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
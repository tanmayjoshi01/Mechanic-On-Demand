package com.mechanicondemand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * MechanicService Entity
 *
 * Junction entity representing the many-to-many relationship between mechanics and services.
 * Allows mechanics to offer services at custom prices and control availability per service.
 */
@Entity
@Table(name = "mechanic_services")
@IdClass(MechanicServiceId.class)
public class MechanicService {

    @Id
    @ManyToOne
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;

    @Id
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "custom_price")
    @DecimalMin(value = "0.0", inclusive = false, message = "Custom price must be greater than 0")
    private BigDecimal customPrice;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    // Constructors
    public MechanicService() {}

    public MechanicService(Mechanic mechanic, Service service) {
        this.mechanic = mechanic;
        this.service = service;
    }

    public MechanicService(Mechanic mechanic, Service service, BigDecimal customPrice) {
        this.mechanic = mechanic;
        this.service = service;
        this.customPrice = customPrice;
    }

    // Getters and Setters
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

    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // Helper method to get the effective price (custom or base)
    public BigDecimal getEffectivePrice() {
        return customPrice != null ? customPrice : service.getBasePrice();
    }
}
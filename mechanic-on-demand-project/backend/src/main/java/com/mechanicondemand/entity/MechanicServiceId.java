package com.mechanicondemand.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Key for MechanicService Entity
 *
 * This class represents the composite primary key consisting of mechanic_id and service_id.
 */
public class MechanicServiceId implements Serializable {

    private Long mechanic;
    private Long service;

    // Default constructor
    public MechanicServiceId() {}

    public MechanicServiceId(Long mechanic, Long service) {
        this.mechanic = mechanic;
        this.service = service;
    }

    // Getters and Setters
    public Long getMechanic() {
        return mechanic;
    }

    public void setMechanic(Long mechanic) {
        this.mechanic = mechanic;
    }

    public Long getService() {
        return service;
    }

    public void setService(Long service) {
        this.service = service;
    }

    // Override equals and hashCode for proper composite key functionality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MechanicServiceId that = (MechanicServiceId) o;
        return Objects.equals(mechanic, that.mechanic) && Objects.equals(service, that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mechanic, service);
    }
}
package com.mechanicondemand.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mechanic_profiles")
public class MechanicProfile {

    @Id
    private Long id; // same as user id

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private boolean available = true;

    @Column(length = 255)
    private String specialties; // e.g. "engine,tires,battery"

    private Double hourlyRate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getSpecialties() { return specialties; }
    public void setSpecialties(String specialties) { this.specialties = specialties; }

    public Double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(Double hourlyRate) { this.hourlyRate = hourlyRate; }
}

package com.mechanicondemand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

/**
 * Mechanic Entity
 *
 * Represents mechanics in the system who provide services to customers.
 * Extends the User entity to inherit common user properties.
 */
@Entity
@Table(name = "mechanics")
@PrimaryKeyJoinColumn(name = "id")
public class Mechanic extends User {

    @Column(name = "experience_years")
    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 50, message = "Experience years cannot exceed 50")
    private Integer experienceYears = 0;

    @Column
    @DecimalMin(value = "0.0", message = "Rating cannot be negative")
    @DecimalMax(value = "5.0", message = "Rating cannot exceed 5.0")
    private Double rating = 0.0;

    @Column(name = "total_jobs_completed")
    @Min(value = 0, message = "Total jobs completed cannot be negative")
    private Integer totalJobsCompleted = 0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "service_radius_km")
    @DecimalMin(value = "1.0", message = "Service radius must be at least 1 km")
    @DecimalMax(value = "100.0", message = "Service radius cannot exceed 100 km")
    private Double serviceRadiusKm = 10.0;

    @Column(name = "current_latitude")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double currentLatitude;

    @Column(name = "current_longitude")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double currentLongitude;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column
    private String specializations; // JSON string of specializations

    // Constructors
    public Mechanic() {
        super();
        setUserType(UserType.MECHANIC);
    }

    public Mechanic(String username, String email, String password, String firstName, String lastName) {
        super(username, email, password, firstName, lastName, UserType.MECHANIC);
    }

    // Getters and Setters
    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalJobsCompleted() {
        return totalJobsCompleted;
    }

    public void setTotalJobsCompleted(Integer totalJobsCompleted) {
        this.totalJobsCompleted = totalJobsCompleted;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Double getServiceRadiusKm() {
        return serviceRadiusKm;
    }

    public void setServiceRadiusKm(Double serviceRadiusKm) {
        this.serviceRadiusKm = serviceRadiusKm;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecializations() {
        return specializations;
    }

    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }

    // Helper method to update rating after job completion
    public void updateRating(Integer newRating) {
        if (this.rating == null || this.rating == 0.0) {
            this.rating = newRating.doubleValue();
        } else {
            // Calculate weighted average
            double totalRatingPoints = this.rating * this.totalJobsCompleted + newRating;
            this.totalJobsCompleted++;
            this.rating = totalRatingPoints / this.totalJobsCompleted;
        }
    }
}
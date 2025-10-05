package com.mechanicondemand.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Booking Request DTO
 *
 * Contains the data required to create a new booking.
 */
public class BookingRequest {

    @NotNull(message = "Mechanic ID is required")
    private Long mechanicId;

    @NotNull(message = "Service ID is required")
    private Long serviceId;

    @NotNull(message = "Scheduled date and time is required")
    private LocalDateTime scheduledDateTime;

    private String customerAddress;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    private Double customerLatitude;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    private Double customerLongitude;

    @DecimalMin(value = "0.1", message = "Estimated duration must be at least 0.1 hours")
    private BigDecimal estimatedDuration;

    private String specialInstructions;

    // Constructors
    public BookingRequest() {}

    // Getters and Setters
    public Long getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Long mechanicId) {
        this.mechanicId = mechanicId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDateTime getScheduledDateTime() {
        return scheduledDateTime;
    }

    public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
        this.scheduledDateTime = scheduledDateTime;
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

    public BigDecimal getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(BigDecimal estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
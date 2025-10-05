package com.mechanicondemand.dto;

import com.mechanicondemand.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Booking Response DTO
 *
 * Contains booking information for API responses.
 * Includes relevant details from related entities.
 */
public class BookingResponse {

    private Long id;
    private BookingStatus status;
    private LocalDateTime scheduledDateTime;
    private BigDecimal totalPrice;
    private String customerName;
    private String mechanicName;
    private String serviceName;
    private String customerAddress;
    private String mechanicNotes;
    private Integer customerRating;
    private Integer mechanicRating;
    private LocalDateTime createdAt;

    // Constructors
    public BookingResponse() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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
}
package com.mechanicondemand.model;

/**
 * Enum for Booking Status
 * Represents different states of a booking
 */
public enum BookingStatus {
    PENDING,        // Customer has created booking, waiting for mechanic response
    ACCEPTED,       // Mechanic has accepted the booking
    REJECTED,       // Mechanic has rejected the booking
    IN_PROGRESS,    // Mechanic is currently working on the vehicle
    COMPLETED,      // Service completed
    CANCELLED       // Booking cancelled by customer
}

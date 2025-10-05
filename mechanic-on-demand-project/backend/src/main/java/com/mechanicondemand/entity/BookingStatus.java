package com.mechanicondemand.entity;

/**
 * Booking Status Enumeration
 *
 * Represents the different states a booking can be in:
 * - PENDING: Booking request sent, waiting for mechanic confirmation
 * - CONFIRMED: Mechanic has accepted the booking
 * - IN_PROGRESS: Mechanic is currently working on the service
 * - COMPLETED: Service has been completed successfully
 * - CANCELLED: Booking has been cancelled by either party
 */
public enum BookingStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
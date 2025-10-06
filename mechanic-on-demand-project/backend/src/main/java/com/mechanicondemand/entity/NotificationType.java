package com.mechanicondemand.entity;

/**
 * Notification Type Enumeration
 *
 * Represents different types of notifications in the system:
 * - BOOKING_REQUEST: New booking request from a customer
 * - BOOKING_CONFIRMED: Mechanic has confirmed a booking
 * - BOOKING_CANCELLED: Booking has been cancelled
 * - MECHANIC_ARRIVED: Mechanic has arrived at the location
 * - JOB_COMPLETED: Service has been completed
 */
public enum NotificationType {
    BOOKING_REQUEST,
    BOOKING_CONFIRMED,
    BOOKING_CANCELLED,
    MECHANIC_ARRIVED,
    JOB_COMPLETED
}
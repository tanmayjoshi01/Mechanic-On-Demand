package com.mechanicondemand.repository;

import com.mechanicondemand.entity.Booking;
import com.mechanicondemand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * BookingRepository - Data Access Layer for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find bookings by customer
     */
    List<Booking> findByCustomerOrderByCreatedAtDesc(User customer);
    
    /**
     * Find bookings by mechanic
     */
    List<Booking> findByMechanicOrderByCreatedAtDesc(User mechanic);
    
    /**
     * Find bookings by status
     */
    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);
    
    /**
     * Find bookings by customer and status
     */
    List<Booking> findByCustomerAndStatusOrderByCreatedAtDesc(User customer, Booking.BookingStatus status);
    
    /**
     * Find bookings by mechanic and status
     */
    List<Booking> findByMechanicAndStatusOrderByCreatedAtDesc(User mechanic, Booking.BookingStatus status);
    
    /**
     * Find bookings by date range
     */
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate ORDER BY b.bookingDate, b.bookingTime")
    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDate startDate, 
                                         @Param("endDate") LocalDate endDate);
    
    /**
     * Find pending bookings for a mechanic
     */
    @Query("SELECT b FROM Booking b WHERE b.mechanic = :mechanic AND b.status = 'PENDING' " +
           "ORDER BY b.bookingDate, b.bookingTime")
    List<Booking> findPendingBookingsForMechanic(@Param("mechanic") User mechanic);
    
    /**
     * Count bookings by status
     */
    long countByStatus(Booking.BookingStatus status);
    
    /**
     * Count bookings by customer
     */
    long countByCustomer(User customer);
    
    /**
     * Count bookings by mechanic
     */
    long countByMechanic(User mechanic);
}
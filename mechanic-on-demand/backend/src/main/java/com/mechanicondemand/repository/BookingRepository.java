package com.mechanicondemand.repository;

import com.mechanicondemand.model.Booking;
import com.mechanicondemand.model.Customer;
import com.mechanicondemand.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 📅 Booking Repository
 * 
 * This interface handles all database operations for bookings.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    /**
     * Find bookings by customer
     */
    List<Booking> findByCustomerOrderByCreatedAtDesc(Customer customer);
    
    /**
     * Find bookings by mechanic
     */
    List<Booking> findByMechanicOrderByCreatedAtDesc(Mechanic mechanic);
    
    /**
     * Find bookings by status
     */
    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);
    
    /**
     * Find bookings by customer and status
     */
    List<Booking> findByCustomerAndStatusOrderByCreatedAtDesc(Customer customer, Booking.BookingStatus status);
    
    /**
     * Find bookings by mechanic and status
     */
    List<Booking> findByMechanicAndStatusOrderByCreatedAtDesc(Mechanic mechanic, Booking.BookingStatus status);
    
    /**
     * Find pending bookings for a mechanic
     */
    @Query("SELECT b FROM Booking b WHERE b.mechanic = :mechanic AND b.status = 'PENDING' ORDER BY b.createdAt ASC")
    List<Booking> findPendingBookingsForMechanic(@Param("mechanic") Mechanic mechanic);
    
    /**
     * Find bookings within date range
     */
    @Query("SELECT b FROM Booking b WHERE b.bookingDate BETWEEN :startDate AND :endDate ORDER BY b.bookingDate ASC")
    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
    
    /**
     * Count bookings by status
     */
    long countByStatus(Booking.BookingStatus status);
    
    /**
     * Find bookings by service type
     */
    List<Booking> findByServiceTypeContainingIgnoreCaseOrderByCreatedAtDesc(String serviceType);
}
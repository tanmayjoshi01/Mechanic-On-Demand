package com.mechanicondemand.repository;

import com.mechanicondemand.model.Booking;
import com.mechanicondemand.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Booking Repository - Interface for database operations on Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Find all bookings for a specific customer
    List<Booking> findByCustomerId(Long customerId);
    
    // Find all bookings for a specific mechanic
    List<Booking> findByMechanicId(Long mechanicId);
    
    // Find bookings by status
    List<Booking> findByStatus(BookingStatus status);
    
    // Find pending bookings for a mechanic
    List<Booking> findByMechanicIdAndStatus(Long mechanicId, BookingStatus status);
    
    // Find customer's bookings by status
    List<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status);
    
    // Get booking history for a customer (completed bookings)
    @Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId " +
           "AND b.status = 'COMPLETED' ORDER BY b.completedTime DESC")
    List<Booking> findCustomerHistory(@Param("customerId") Long customerId);
    
    // Get job history for a mechanic
    @Query("SELECT b FROM Booking b WHERE b.mechanic.id = :mechanicId " +
           "AND b.status = 'COMPLETED' ORDER BY b.completedTime DESC")
    List<Booking> findMechanicJobHistory(@Param("mechanicId") Long mechanicId);
    
    // Get monthly bookings for analytics
    @Query("SELECT b FROM Booking b WHERE b.createdAt >= :startDate " +
           "AND b.createdAt < :endDate")
    List<Booking> findBookingsByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}

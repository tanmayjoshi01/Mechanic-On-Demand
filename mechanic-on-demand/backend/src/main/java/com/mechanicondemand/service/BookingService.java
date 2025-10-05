package com.mechanicondemand.service;

import com.mechanicondemand.model.Booking;
import com.mechanicondemand.model.Customer;
import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 📅 Booking Service
 * 
 * This class contains all business logic related to bookings.
 */
@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private MechanicService mechanicService;
    
    /**
     * Create a new booking
     */
    public Booking createBooking(Long customerId, Long mechanicId, String serviceType, 
                               String description, String vehicleInfo, LocalDateTime bookingDate) {
        
        // Get customer and mechanic
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        
        Mechanic mechanic = mechanicService.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found with ID: " + mechanicId));
        
        // Check if mechanic is available
        if (!mechanic.getIsAvailable()) {
            throw new RuntimeException("Mechanic is not available");
        }
        
        // Create new booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setMechanic(mechanic);
        booking.setServiceType(serviceType);
        booking.setDescription(description);
        booking.setVehicleInfo(vehicleInfo);
        booking.setBookingDate(bookingDate);
        booking.setStatus(Booking.BookingStatus.PENDING);
        
        // Calculate estimated cost (simplified)
        if (mechanic.getHourlyRate() != null) {
            booking.setEstimatedDuration(60); // Default 1 hour
            booking.setTotalCost(mechanic.getHourlyRate());
        }
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Get booking by ID
     */
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }
    
    /**
     * Get all bookings for a customer
     */
    public List<Booking> getCustomerBookings(Long customerId) {
        Customer customer = customerService.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
        return bookingRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }
    
    /**
     * Get all bookings for a mechanic
     */
    public List<Booking> getMechanicBookings(Long mechanicId) {
        Mechanic mechanic = mechanicService.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found with ID: " + mechanicId));
        return bookingRepository.findByMechanicOrderByCreatedAtDesc(mechanic);
    }
    
    /**
     * Get pending bookings for a mechanic
     */
    public List<Booking> getPendingBookingsForMechanic(Long mechanicId) {
        Mechanic mechanic = mechanicService.findById(mechanicId)
                .orElseThrow(() -> new RuntimeException("Mechanic not found with ID: " + mechanicId));
        return bookingRepository.findPendingBookingsForMechanic(mechanic);
    }
    
    /**
     * Update booking status
     */
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
    
    /**
     * Accept booking (mechanic accepts)
     */
    public Booking acceptBooking(Long bookingId, Long mechanicId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        // Verify mechanic owns this booking
        if (!booking.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic does not have permission to accept this booking");
        }
        
        booking.setStatus(Booking.BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Reject booking (mechanic rejects)
     */
    public Booking rejectBooking(Long bookingId, Long mechanicId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        // Verify mechanic owns this booking
        if (!booking.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic does not have permission to reject this booking");
        }
        
        booking.setStatus(Booking.BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Complete booking
     */
    public Booking completeBooking(Long bookingId, Long mechanicId, Integer actualDuration) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        // Verify mechanic owns this booking
        if (!booking.getMechanic().getId().equals(mechanicId)) {
            throw new RuntimeException("Mechanic does not have permission to complete this booking");
        }
        
        booking.setStatus(Booking.BookingStatus.COMPLETED);
        booking.setActualDuration(actualDuration);
        
        // Recalculate cost based on actual duration
        if (booking.getMechanic().getHourlyRate() != null && actualDuration != null) {
            BigDecimal hourlyRate = booking.getMechanic().getHourlyRate();
            BigDecimal durationInHours = BigDecimal.valueOf(actualDuration).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
            booking.setTotalCost(hourlyRate.multiply(durationInHours));
        }
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Rate and review booking
     */
    public Booking rateBooking(Long bookingId, Long customerId, Integer rating, String feedback) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        // Verify customer owns this booking
        if (!booking.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Customer does not have permission to rate this booking");
        }
        
        // Check if booking is completed
        if (booking.getStatus() != Booking.BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed bookings");
        }
        
        booking.setCustomerRating(rating);
        booking.setCustomerFeedback(feedback);
        
        // Update mechanic rating
        mechanicService.updateRating(booking.getMechanic().getId(), rating);
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Cancel booking
     */
    public Booking cancelBooking(Long bookingId, Long userId, String userType) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
        
        // Verify user has permission to cancel
        boolean canCancel = false;
        if ("CUSTOMER".equals(userType) && booking.getCustomer().getId().equals(userId)) {
            canCancel = true;
        } else if ("MECHANIC".equals(userType) && booking.getMechanic().getId().equals(userId)) {
            canCancel = true;
        }
        
        if (!canCancel) {
            throw new RuntimeException("User does not have permission to cancel this booking");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * Get bookings by status
     */
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatusOrderByCreatedAtDesc(status);
    }
}
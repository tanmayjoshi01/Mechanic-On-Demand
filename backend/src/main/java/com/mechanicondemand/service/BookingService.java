package com.mechanicondemand.service;

import com.mechanicondemand.dto.BookingRequest;
import com.mechanicondemand.model.*;
import com.mechanicondemand.repository.BookingRepository;
import com.mechanicondemand.repository.CustomerRepository;
import com.mechanicondemand.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Booking Service - Handles all booking-related business logic
 */
@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private MechanicRepository mechanicRepository;
    
    /**
     * Create a new booking
     */
    public Booking createBooking(BookingRequest request) {
        // Find customer and mechanic
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        Mechanic mechanic = mechanicRepository.findById(request.getMechanicId())
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));
        
        // Calculate price based on subscription type
        Double price = calculatePrice(mechanic, request.getSubscriptionType());
        
        // Create booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setMechanic(mechanic);
        booking.setVehicleType(request.getVehicleType());
        booking.setVehicleModel(request.getVehicleModel());
        booking.setProblemDescription(request.getProblemDescription());
        booking.setLocation(request.getLocation());
        booking.setLatitude(request.getLatitude());
        booking.setLongitude(request.getLongitude());
        booking.setSubscriptionType(request.getSubscriptionType());
        booking.setPrice(price);
        booking.setScheduledTime(request.getScheduledTime() != null ? 
                                 request.getScheduledTime() : LocalDateTime.now());
        booking.setStatus(BookingStatus.PENDING);
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Calculate price based on subscription type
     */
    private Double calculatePrice(Mechanic mechanic, SubscriptionType subscriptionType) {
        switch (subscriptionType) {
            case HOURLY:
                return mechanic.getHourlyRate();
            case MONTHLY:
                return mechanic.getMonthlySubscription();
            case YEARLY:
                return mechanic.getYearlySubscription();
            default:
                return mechanic.getHourlyRate();
        }
    }
    
    /**
     * Get booking by ID
     */
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
    
    /**
     * Get all bookings for a customer
     */
    public List<Booking> getCustomerBookings(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }
    
    /**
     * Get all bookings for a mechanic
     */
    public List<Booking> getMechanicBookings(Long mechanicId) {
        return bookingRepository.findByMechanicId(mechanicId);
    }
    
    /**
     * Accept a booking (Mechanic action)
     */
    public Booking acceptBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending state");
        }
        booking.setStatus(BookingStatus.ACCEPTED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Reject a booking (Mechanic action)
     */
    public Booking rejectBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking is not in pending state");
        }
        booking.setStatus(BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Start working on a booking (Mechanic action)
     */
    public Booking startBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new RuntimeException("Booking must be accepted first");
        }
        booking.setStatus(BookingStatus.IN_PROGRESS);
        
        // Update mechanic availability
        Mechanic mechanic = booking.getMechanic();
        mechanic.setAvailable(false);
        mechanicRepository.save(mechanic);
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Complete a booking (Mechanic action)
     */
    public Booking completeBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.COMPLETED);
        booking.setCompletedTime(LocalDateTime.now());
        
        // Update mechanic availability and job count
        Mechanic mechanic = booking.getMechanic();
        mechanic.setAvailable(true);
        mechanic.setTotalJobs(mechanic.getTotalJobs() + 1);
        mechanicRepository.save(mechanic);
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Cancel a booking (Customer action)
     */
    public Booking cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed booking");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }
    
    /**
     * Rate a completed booking (Customer action)
     */
    public Booking rateBooking(Long bookingId, Integer rating, String feedback) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed bookings");
        }
        
        booking.setRating(rating);
        booking.setFeedback(feedback);
        
        // Update mechanic rating
        Mechanic mechanic = booking.getMechanic();
        List<Booking> completedBookings = bookingRepository.findMechanicJobHistory(mechanic.getId());
        double totalRating = completedBookings.stream()
            .filter(b -> b.getRating() != null)
            .mapToInt(Booking::getRating)
            .average()
            .orElse(0.0);
        mechanic.setRating(totalRating);
        mechanicRepository.save(mechanic);
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Get all bookings
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}

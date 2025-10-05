package com.mechanicondemand.controller;

import com.mechanicondemand.model.Booking;
import com.mechanicondemand.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📅 Booking Controller
 * 
 * This controller handles all booking-related endpoints.
 * It manages the booking lifecycle from creation to completion.
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * Create a new booking
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody Map<String, Object> bookingRequest) {
        try {
            Long customerId = Long.valueOf(bookingRequest.get("customerId").toString());
            Long mechanicId = Long.valueOf(bookingRequest.get("mechanicId").toString());
            String serviceType = bookingRequest.get("serviceType").toString();
            String description = bookingRequest.get("description").toString();
            String vehicleInfo = bookingRequest.get("vehicleInfo").toString();
            
            // Parse booking date
            LocalDateTime bookingDate = LocalDateTime.parse(bookingRequest.get("bookingDate").toString());
            
            Booking booking = bookingService.createBooking(
                customerId, mechanicId, serviceType, description, vehicleInfo, bookingDate
            );
            
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get all bookings
     * GET /api/bookings
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get booking by ID
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get customer bookings
     * GET /api/bookings/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getCustomerBookings(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getCustomerBookings(customerId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get mechanic bookings
     * GET /api/bookings/mechanic/{mechanicId}
     */
    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<List<Booking>> getMechanicBookings(@PathVariable Long mechanicId) {
        List<Booking> bookings = bookingService.getMechanicBookings(mechanicId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get pending bookings for mechanic
     * GET /api/bookings/mechanic/{mechanicId}/pending
     */
    @GetMapping("/mechanic/{mechanicId}/pending")
    public ResponseEntity<List<Booking>> getPendingBookingsForMechanic(@PathVariable Long mechanicId) {
        List<Booking> bookings = bookingService.getPendingBookingsForMechanic(mechanicId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Accept booking
     * PUT /api/bookings/{id}/accept
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptBooking(@PathVariable Long id, 
                                         @RequestBody Map<String, Long> request) {
        try {
            Long mechanicId = request.get("mechanicId");
            Booking booking = bookingService.acceptBooking(id, mechanicId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Reject booking
     * PUT /api/bookings/{id}/reject
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id, 
                                         @RequestBody Map<String, Long> request) {
        try {
            Long mechanicId = request.get("mechanicId");
            Booking booking = bookingService.rejectBooking(id, mechanicId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Complete booking
     * PUT /api/bookings/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id, 
                                           @RequestBody Map<String, Object> request) {
        try {
            Long mechanicId = Long.valueOf(request.get("mechanicId").toString());
            Integer actualDuration = Integer.valueOf(request.get("actualDuration").toString());
            
            Booking booking = bookingService.completeBooking(id, mechanicId, actualDuration);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Rate booking
     * PUT /api/bookings/{id}/rate
     */
    @PutMapping("/{id}/rate")
    public ResponseEntity<?> rateBooking(@PathVariable Long id, 
                                       @RequestBody Map<String, Object> request) {
        try {
            Long customerId = Long.valueOf(request.get("customerId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            String feedback = request.get("feedback").toString();
            
            Booking booking = bookingService.rateBooking(id, customerId, rating, feedback);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Cancel booking
     * PUT /api/bookings/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, 
                                         @RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String userType = request.get("userType").toString();
            
            Booking booking = bookingService.cancelBooking(id, userId, userType);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Get bookings by status
     * GET /api/bookings/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        try {
            Booking.BookingStatus bookingStatus = Booking.BookingStatus.valueOf(status.toUpperCase());
            List<Booking> bookings = bookingService.getBookingsByStatus(bookingStatus);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
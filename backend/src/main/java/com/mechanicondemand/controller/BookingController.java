package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.dto.BookingRequest;
import com.mechanicondemand.model.Booking;
import com.mechanicondemand.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Booking Controller - Handles all booking-related endpoints
 * 
 * REST API Endpoints:
 * POST /api/bookings - Create new booking
 * GET /api/bookings - Get all bookings
 * GET /api/bookings/{id} - Get specific booking
 * GET /api/bookings/customer/{customerId} - Get customer's bookings
 * GET /api/bookings/mechanic/{mechanicId} - Get mechanic's bookings
 * PUT /api/bookings/{id}/accept - Accept booking
 * PUT /api/bookings/{id}/reject - Reject booking
 * PUT /api/bookings/{id}/start - Start working on booking
 * PUT /api/bookings/{id}/complete - Complete booking
 * PUT /api/bookings/{id}/cancel - Cancel booking
 * PUT /api/bookings/{id}/rate - Rate completed booking
 */
@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    /**
     * POST /api/bookings
     * Create a new booking
     * 
     * HTTP Method: POST - Creates a new booking resource
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Booking created successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/bookings
     * Get all bookings (Admin function)
     */
    @GetMapping
    public ResponseEntity<?> getAllBookings() {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(new ApiResponse(true, "Bookings retrieved", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/bookings/{id}
     * Get booking by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Booking booking = bookingService.getBookingById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking found", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/bookings/customer/{customerId}
     * Get all bookings for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerBookings(@PathVariable Long customerId) {
        try {
            List<Booking> bookings = bookingService.getCustomerBookings(customerId);
            return ResponseEntity.ok(new ApiResponse(true, "Customer bookings retrieved", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/bookings/mechanic/{mechanicId}
     * Get all bookings for a mechanic
     */
    @GetMapping("/mechanic/{mechanicId}")
    public ResponseEntity<?> getMechanicBookings(@PathVariable Long mechanicId) {
        try {
            List<Booking> bookings = bookingService.getMechanicBookings(mechanicId);
            return ResponseEntity.ok(new ApiResponse(true, "Mechanic bookings retrieved", bookings));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/accept
     * Mechanic accepts a booking
     * 
     * HTTP Method: PUT - Updates booking status
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.acceptBooking(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking accepted", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/reject
     * Mechanic rejects a booking
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.rejectBooking(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking rejected", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/start
     * Mechanic starts working on a booking
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.startBooking(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking started", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/complete
     * Mechanic completes a booking
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.completeBooking(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking completed", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/cancel
     * Customer cancels a booking
     * 
     * HTTP Method: PUT - Updates booking status
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            Booking booking = bookingService.cancelBooking(id);
            return ResponseEntity.ok(new ApiResponse(true, "Booking cancelled", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/bookings/{id}/rate
     * Customer rates a completed booking
     * 
     * Request Params: rating (1-5), feedback (optional)
     */
    @PutMapping("/{id}/rate")
    public ResponseEntity<?> rateBooking(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam(required = false) String feedback) {
        try {
            Booking booking = bookingService.rateBooking(id, rating, feedback);
            return ResponseEntity.ok(new ApiResponse(true, "Booking rated successfully", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}

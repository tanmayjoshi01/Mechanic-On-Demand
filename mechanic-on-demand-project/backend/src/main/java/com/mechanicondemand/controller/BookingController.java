package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.dto.BookingRequest;
import com.mechanicondemand.dto.BookingResponse;
import com.mechanicondemand.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Booking Controller
 *
 * Handles booking-related operations including:
 * - Creating new bookings
 * - Retrieving bookings (for both customers and mechanics)
 * - Updating booking status
 * - Rating completed bookings
 * - Cancelling bookings
 *
 * Demonstrates complex business logic for booking management.
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Create New Booking
     *
     * POST /api/bookings
     *
     * Creates a new service booking request.
     *
     * Request Body:
     * {
     *   "mechanicId": 1,
     *   "serviceId": 2,
     *   "scheduledDateTime": "2024-01-15T10:00:00",
     *   "customerAddress": "123 Main St, City, State",
     *   "customerLatitude": 40.7128,
     *   "customerLongitude": -74.0060,
     *   "estimatedDuration": 2.5,
     *   "specialInstructions": "Engine making strange noise"
     * }
     *
     * Response (201 Created):
     * {
     *   "success": true,
     *   "message": "Booking created successfully",
     *   "data": {
     *     "id": 1,
     *     "status": "PENDING",
     *     "scheduledDateTime": "2024-01-15T10:00:00",
     *     "totalPrice": 150.00,
     *     "mechanicName": "John Smith",
     *     "serviceName": "Engine Repair"
     *   }
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            Authentication authentication,
            @Valid @RequestBody BookingRequest bookingRequest) {
        try {
            String customerEmail = authentication.getName();
            BookingResponse bookingResponse = bookingService.createBooking(customerEmail, bookingRequest);

            ApiResponse<BookingResponse> response = ApiResponse.success("Booking created successfully", bookingResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<BookingResponse> response = ApiResponse.error("Failed to create booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Get Customer's Bookings
     *
     * GET /api/bookings?status={status}&page={page}&size={size}
     *
     * Retrieves the current customer's bookings with optional filtering.
     *
     * Query Parameters:
     * - status: string (optional) - Filter by booking status
     * - page: int (optional) - Page number for pagination (default: 0)
     * - size: int (optional) - Page size for pagination (default: 10)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getBookings(
            Authentication authentication,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String userEmail = authentication.getName();
            List<BookingResponse> bookings = bookingService.getBookingsForUser(userEmail, status, page, size);

            ApiResponse<List<BookingResponse>> response = ApiResponse.success(
                "Bookings retrieved successfully", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<BookingResponse>> response = ApiResponse.error(
                "Failed to retrieve bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get Booking by ID
     *
     * GET /api/bookings/{id}
     *
     * Retrieves a specific booking by its ID.
     * Users can only access their own bookings or bookings assigned to them as mechanics.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            String userEmail = authentication.getName();
            BookingResponse booking = bookingService.getBookingById(userEmail, id);

            if (booking != null) {
                ApiResponse<BookingResponse> response = ApiResponse.success(
                    "Booking retrieved successfully", booking);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<BookingResponse> response = ApiResponse.error(
                    "Booking not found or access denied", "BOOKING_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<BookingResponse> response = ApiResponse.error(
                "Failed to retrieve booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update Booking Status (Mechanic only)
     *
     * PUT /api/bookings/{id}/status
     *
     * Updates the status of a booking (typically by the assigned mechanic).
     *
     * Request Body:
     * {
     *   "status": "CONFIRMED"
     * }
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateBookingStatus(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest statusRequest) {
        try {
            String mechanicEmail = authentication.getName();
            String message = bookingService.updateBookingStatus(mechanicEmail, id, statusRequest.getStatus());

            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error(
                "Failed to update booking status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Rate Completed Booking
     *
     * POST /api/bookings/{id}/rate
     *
     * Allows customers to rate completed bookings and mechanics to rate customers.
     *
     * Request Body:
     * {
     *   "rating": 5,
     *   "comment": "Excellent service, very professional!"
     * }
     */
    @PostMapping("/{id}/rate")
    public ResponseEntity<ApiResponse<String>> rateBooking(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody RatingRequest ratingRequest) {
        try {
            String userEmail = authentication.getName();
            String message = bookingService.rateBooking(userEmail, id, ratingRequest.getRating(), ratingRequest.getComment());

            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error(
                "Failed to rate booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Cancel Booking
     *
     * DELETE /api/bookings/{id}
     *
     * Cancels a booking. Different rules apply based on timing and user role.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            Authentication authentication,
            @PathVariable Long id) {
        try {
            String userEmail = authentication.getName();
            String message = bookingService.cancelBooking(userEmail, id);

            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error(
                "Failed to cancel booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // DTOs for request/response
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class RatingRequest {
        private Integer rating;
        private String comment;

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }

        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}
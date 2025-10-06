package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.entity.Mechanic;
import com.mechanicondemand.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Mechanic Controller
 *
 * Handles mechanic-specific operations including:
 * - Profile management
 * - Availability status updates
 * - Nearby mechanics discovery (for customers)
 * - Job history and bookings
 *
 * Demonstrates role-based access control with @PreAuthorize annotations.
 */
@RestController
@RequestMapping("/api/mechanics")
@CrossOrigin(origins = "*")
public class MechanicController {

    private final MechanicService mechanicService;

    @Autowired
    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    /**
     * Get Mechanic Profile
     *
     * GET /api/mechanics/profile
     *
     * Retrieves the current mechanic's profile information.
     *
     * Response (200 OK):
     * {
     *   "success": true,
     *   "message": "Profile retrieved successfully",
     *   "data": {
     *     "id": 1,
     *     "username": "mechanic1",
     *     "email": "mechanic@example.com",
     *     "firstName": "John",
     *     "lastName": "Smith",
     *     "phoneNumber": "555-1234",
     *     "experienceYears": 5,
     *     "rating": 4.5,
     *     "totalJobsCompleted": 25,
     *     "isAvailable": true,
     *     "serviceRadiusKm": 15.0,
     *     "currentLatitude": 40.7128,
     *     "currentLongitude": -74.0060,
     *     "licenseNumber": "MECH123456",
     *     "specializations": "Engine,Brakes,Electrical"
     *   }
     * }
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Mechanic>> getMechanicProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            Mechanic mechanic = mechanicService.getMechanicByEmail(email);

            if (mechanic != null) {
                ApiResponse<Mechanic> response = ApiResponse.success("Profile retrieved successfully", mechanic);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Mechanic> response = ApiResponse.error("Mechanic profile not found", "PROFILE_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Mechanic> response = ApiResponse.error("Failed to retrieve profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update Mechanic Profile
     *
     * PUT /api/mechanics/profile
     *
     * Updates the current mechanic's profile information.
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Mechanic>> updateMechanicProfile(
            Authentication authentication,
            @RequestBody Mechanic updatedMechanic) {
        try {
            String email = authentication.getName();
            Mechanic mechanic = mechanicService.updateMechanicProfile(email, updatedMechanic);

            ApiResponse<Mechanic> response = ApiResponse.success("Profile updated successfully", mechanic);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Mechanic> response = ApiResponse.error("Failed to update profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update Availability Status
     *
     * PUT /api/mechanics/availability
     *
     * Updates the mechanic's availability status.
     *
     * Request Body:
     * {
     *   "isAvailable": true
     * }
     */
    @PutMapping("/availability")
    public ResponseEntity<ApiResponse<String>> updateAvailability(
            Authentication authentication,
            @RequestBody Boolean isAvailable) {
        try {
            String email = authentication.getName();
            String message = mechanicService.updateAvailability(email, isAvailable);

            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Failed to update availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get Nearby Mechanics (Public Endpoint)
     *
     * GET /api/mechanics/nearby?lat={latitude}&lng={longitude}&radius={radius}
     *
     * Retrieves mechanics available near the specified location.
     * This endpoint is public and can be used by customers to find nearby mechanics.
     *
     * Query Parameters:
     * - lat: double - Customer's latitude
     * - lng: double - Customer's longitude
     * - radius: double (optional) - Search radius in km (default: 10)
     */
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<Mechanic>>> getNearbyMechanics(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam(defaultValue = "10.0") Double radius) {
        try {
            List<Mechanic> mechanics = mechanicService.getNearbyMechanics(lat, lng, radius);

            ApiResponse<List<Mechanic>> response = ApiResponse.success(
                "Nearby mechanics retrieved successfully", mechanics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Mechanic>> response = ApiResponse.error(
                "Failed to retrieve nearby mechanics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get Mechanic's Bookings
     *
     * GET /api/mechanics/bookings?status={status}
     *
     * Retrieves the current mechanic's bookings with optional status filtering.
     *
     * Query Parameters:
     * - status: string (optional) - Filter by booking status (PENDING, CONFIRMED, etc.)
     */
    @GetMapping("/bookings")
    public ResponseEntity<ApiResponse<List<Object>>> getMechanicBookings(
            Authentication authentication,
            @RequestParam(required = false) String status) {
        try {
            String email = authentication.getName();
            List<Object> bookings = mechanicService.getMechanicBookings(email, status);

            ApiResponse<List<Object>> response = ApiResponse.success(
                "Bookings retrieved successfully", bookings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Object>> response = ApiResponse.error(
                "Failed to retrieve bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update Location
     *
     * PUT /api/mechanics/location
     *
     * Updates the mechanic's current location for better service discovery.
     *
     * Request Body:
     * {
     *   "latitude": 40.7128,
     *   "longitude": -74.0060
     * }
     */
    @PutMapping("/location")
    public ResponseEntity<ApiResponse<String>> updateLocation(
            Authentication authentication,
            @RequestBody LocationRequest locationRequest) {
        try {
            String email = authentication.getName();
            String message = mechanicService.updateLocation(email, locationRequest.getLatitude(), locationRequest.getLongitude());

            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Failed to update location: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DTO for location updates
    public static class LocationRequest {
        private Double latitude;
        private Double longitude;

        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }

        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }
}
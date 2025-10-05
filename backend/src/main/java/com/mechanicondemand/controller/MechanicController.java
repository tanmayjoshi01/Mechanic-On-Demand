package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Mechanic Controller - Handles mechanic-related endpoints
 * 
 * REST API Endpoints:
 * GET /api/mechanics - Get all mechanics
 * GET /api/mechanics/{id} - Get specific mechanic
 * GET /api/mechanics/available - Get available mechanics
 * GET /api/mechanics/nearby - Find nearby mechanics
 * PUT /api/mechanics/{id}/availability - Update availability
 * PUT /api/mechanics/{id} - Update mechanic profile
 */
@RestController
@RequestMapping("/mechanics")
@CrossOrigin(origins = "*")
public class MechanicController {
    
    @Autowired
    private MechanicService mechanicService;
    
    /**
     * GET /api/mechanics
     * Get all mechanics
     * 
     * HTTP Method: GET - Used to retrieve data (no modification)
     */
    @GetMapping
    public ResponseEntity<?> getAllMechanics() {
        try {
            List<Mechanic> mechanics = mechanicService.getAllMechanics();
            return ResponseEntity.ok(new ApiResponse(true, "Mechanics retrieved successfully", mechanics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/mechanics/{id}
     * Get mechanic by ID
     * 
     * Path Variable: id - Mechanic ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMechanicById(@PathVariable Long id) {
        try {
            Mechanic mechanic = mechanicService.getMechanicById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Mechanic found", mechanic));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/mechanics/available
     * Get all available mechanics
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableMechanics() {
        try {
            List<Mechanic> mechanics = mechanicService.getAvailableMechanics();
            return ResponseEntity.ok(new ApiResponse(true, "Available mechanics retrieved", mechanics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * GET /api/mechanics/nearby
     * Find mechanics near customer location
     * 
     * Query Parameters:
     * - latitude: Customer's latitude
     * - longitude: Customer's longitude
     * - radius: Search radius in kilometers (default: 10 km)
     */
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyMechanics(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "10.0") Double radius) {
        try {
            List<Mechanic> mechanics = mechanicService.findNearbyMechanics(latitude, longitude, radius);
            return ResponseEntity.ok(new ApiResponse(true, "Nearby mechanics found", mechanics));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/mechanics/{id}/availability
     * Update mechanic availability status
     * 
     * HTTP Method: PUT - Used to update existing resources
     * Path Variable: id - Mechanic ID
     * Request Param: available - true or false
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {
        try {
            Mechanic mechanic = mechanicService.updateAvailability(id, available);
            return ResponseEntity.ok(new ApiResponse(true, "Availability updated", mechanic));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * PUT /api/mechanics/{id}
     * Update mechanic profile
     * 
     * HTTP Method: PUT - Used to update existing resources
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMechanic(
            @PathVariable Long id,
            @RequestBody Mechanic mechanic) {
        try {
            Mechanic updatedMechanic = mechanicService.updateMechanic(id, mechanic);
            return ResponseEntity.ok(new ApiResponse(true, "Mechanic updated successfully", updatedMechanic));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}

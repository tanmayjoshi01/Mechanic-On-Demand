package com.mechanicondemand.controller;

import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🔧 Mechanic Controller
 * 
 * This controller handles all mechanic-related endpoints.
 * 
 * HTTP Methods Explained:
 * - GET: Retrieve data (read)
 * - POST: Create new data
 * - PUT: Update existing data
 * - DELETE: Remove data
 */
@RestController
@RequestMapping("/api/mechanics")
@CrossOrigin(origins = "*")
public class MechanicController {
    
    @Autowired
    private MechanicService mechanicService;
    
    /**
     * Get all mechanics
     * GET /api/mechanics
     */
    @GetMapping
    public ResponseEntity<List<Mechanic>> getAllMechanics() {
        List<Mechanic> mechanics = mechanicService.getAllMechanics();
        return ResponseEntity.ok(mechanics);
    }
    
    /**
     * Get available mechanics
     * GET /api/mechanics/available
     */
    @GetMapping("/available")
    public ResponseEntity<List<Mechanic>> getAvailableMechanics() {
        List<Mechanic> mechanics = mechanicService.getAvailableMechanics();
        return ResponseEntity.ok(mechanics);
    }
    
    /**
     * Find nearby mechanics
     * GET /api/mechanics/nearby?lat=40.7128&lng=-74.0060&radius=10
     */
    @GetMapping("/nearby")
    public ResponseEntity<?> findNearbyMechanics(
            @RequestParam BigDecimal lat,
            @RequestParam BigDecimal lng,
            @RequestParam(defaultValue = "10") double radius) {
        try {
            List<Mechanic> mechanics = mechanicService.findNearbyMechanics(lat, lng, radius);
            return ResponseEntity.ok(mechanics);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Find mechanics by specialization
     * GET /api/mechanics/specialization?specialization=Engine
     */
    @GetMapping("/specialization")
    public ResponseEntity<List<Mechanic>> findMechanicsBySpecialization(
            @RequestParam String specialization) {
        List<Mechanic> mechanics = mechanicService.findMechanicsBySpecialization(specialization);
        return ResponseEntity.ok(mechanics);
    }
    
    /**
     * Find mechanics by rate range
     * GET /api/mechanics/rate-range?minRate=50&maxRate=100
     */
    @GetMapping("/rate-range")
    public ResponseEntity<List<Mechanic>> findMechanicsByRateRange(
            @RequestParam BigDecimal minRate,
            @RequestParam BigDecimal maxRate) {
        List<Mechanic> mechanics = mechanicService.findMechanicsByRateRange(minRate, maxRate);
        return ResponseEntity.ok(mechanics);
    }
    
    /**
     * Find mechanics by minimum rating
     * GET /api/mechanics/rating?minRating=4.0
     */
    @GetMapping("/rating")
    public ResponseEntity<List<Mechanic>> findMechanicsByMinRating(
            @RequestParam BigDecimal minRating) {
        List<Mechanic> mechanics = mechanicService.findMechanicsByMinRating(minRating);
        return ResponseEntity.ok(mechanics);
    }
    
    /**
     * Get mechanic by ID
     * GET /api/mechanics/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMechanicById(@PathVariable Long id) {
        try {
            Mechanic mechanic = mechanicService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mechanic not found with ID: " + id));
            return ResponseEntity.ok(mechanic);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update mechanic availability
     * PUT /api/mechanics/{id}/availability
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable Long id, 
                                              @RequestBody Map<String, Boolean> request) {
        try {
            Boolean isAvailable = request.get("isAvailable");
            Mechanic mechanic = mechanicService.updateAvailability(id, isAvailable);
            return ResponseEntity.ok(mechanic);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update mechanic profile
     * PUT /api/mechanics/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMechanic(@PathVariable Long id, 
                                         @Valid @RequestBody Mechanic mechanicDetails) {
        try {
            Mechanic existingMechanic = mechanicService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mechanic not found with ID: " + id));
            
            // Update fields
            existingMechanic.setFirstName(mechanicDetails.getFirstName());
            existingMechanic.setLastName(mechanicDetails.getLastName());
            existingMechanic.setPhone(mechanicDetails.getPhone());
            existingMechanic.setAddress(mechanicDetails.getAddress());
            existingMechanic.setSpecialization(mechanicDetails.getSpecialization());
            existingMechanic.setHourlyRate(mechanicDetails.getHourlyRate());
            
            Mechanic updatedMechanic = mechanicService.updateMechanic(existingMechanic);
            return ResponseEntity.ok(updatedMechanic);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
package com.mechanicondemand.controller;

import com.mechanicondemand.model.Pricing;
import com.mechanicondemand.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 💰 Pricing Controller
 * 
 * This controller handles all pricing-related endpoints.
 * It provides information about service costs and pricing plans.
 */
@RestController
@RequestMapping("/api/pricing")
@CrossOrigin(origins = "*")
public class PricingController {
    
    @Autowired
    private PricingService pricingService;
    
    /**
     * Get all pricing options
     * GET /api/pricing
     */
    @GetMapping
    public ResponseEntity<List<Pricing>> getAllPricing() {
        List<Pricing> pricing = pricingService.getAllPricing();
        return ResponseEntity.ok(pricing);
    }
    
    /**
     * Get pricing by service type
     * GET /api/pricing/{serviceType}
     */
    @GetMapping("/{serviceType}")
    public ResponseEntity<?> getPricingByServiceType(@PathVariable String serviceType) {
        try {
            Pricing pricing = pricingService.getPricingByServiceType(serviceType)
                    .orElseThrow(() -> new RuntimeException("Pricing not found for service type: " + serviceType));
            return ResponseEntity.ok(pricing);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Create new pricing option
     * POST /api/pricing
     */
    @PostMapping
    public ResponseEntity<?> createPricing(@Valid @RequestBody Pricing pricing) {
        try {
            Pricing savedPricing = pricingService.createPricing(pricing);
            return ResponseEntity.ok(savedPricing);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Update pricing
     * PUT /api/pricing/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePricing(@PathVariable Long id, @Valid @RequestBody Pricing pricing) {
        try {
            pricing.setId(id);
            Pricing updatedPricing = pricingService.updatePricing(pricing);
            return ResponseEntity.ok(updatedPricing);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Delete pricing
     * DELETE /api/pricing/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePricing(@PathVariable Long id) {
        try {
            pricingService.deletePricing(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Pricing deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
package com.mechanicondemand.service;

import com.mechanicondemand.model.Pricing;
import com.mechanicondemand.repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 💰 Pricing Service
 * 
 * This class contains all business logic related to pricing.
 */
@Service
public class PricingService {
    
    @Autowired
    private PricingRepository pricingRepository;
    
    /**
     * Get all pricing options
     */
    public List<Pricing> getAllPricing() {
        return pricingRepository.findAllByOrderByServiceTypeAsc();
    }
    
    /**
     * Get pricing by service type
     */
    public Optional<Pricing> getPricingByServiceType(String serviceType) {
        return pricingRepository.findByServiceType(serviceType);
    }
    
    /**
     * Create new pricing option
     */
    public Pricing createPricing(Pricing pricing) {
        // Check if pricing already exists for this service type
        if (pricingRepository.existsByServiceType(pricing.getServiceType())) {
            throw new RuntimeException("Pricing already exists for service type: " + pricing.getServiceType());
        }
        
        return pricingRepository.save(pricing);
    }
    
    /**
     * Update pricing
     */
    public Pricing updatePricing(Pricing pricing) {
        return pricingRepository.save(pricing);
    }
    
    /**
     * Delete pricing
     */
    public void deletePricing(Long id) {
        pricingRepository.deleteById(id);
    }
}
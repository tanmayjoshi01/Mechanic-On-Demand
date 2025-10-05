package com.mechanicondemand.repository;

import com.mechanicondemand.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 💰 Pricing Repository
 * 
 * This interface handles all database operations for pricing.
 */
@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    
    /**
     * Find pricing by service type
     */
    Optional<Pricing> findByServiceType(String serviceType);
    
    /**
     * Find all pricing options ordered by service type
     */
    List<Pricing> findAllByOrderByServiceTypeAsc();
    
    /**
     * Check if pricing exists for service type
     */
    boolean existsByServiceType(String serviceType);
}
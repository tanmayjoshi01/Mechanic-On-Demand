package com.mechanicondemand.repository;

import com.mechanicondemand.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 🔧 Mechanic Repository
 * 
 * This interface handles all database operations for mechanics.
 * Spring Data JPA provides automatic implementations.
 */
@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    
    /**
     * Find mechanic by email
     */
    Optional<Mechanic> findByEmail(String email);
    
    /**
     * Check if mechanic exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Find available mechanics
     */
    List<Mechanic> findByIsAvailableTrue();
    
    /**
     * Find mechanics by specialization
     */
    List<Mechanic> findBySpecializationContainingIgnoreCase(String specialization);
    
    /**
     * Find nearby mechanics using Haversine formula
     * This is a custom query to find mechanics within a certain radius
     */
    @Query(value = "SELECT * FROM mechanics m WHERE " +
            "m.is_available = true AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(m.latitude)) * " +
            "cos(radians(m.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(m.latitude)))) <= :radius " +
            "ORDER BY (6371 * acos(cos(radians(:lat)) * cos(radians(m.latitude)) * " +
            "cos(radians(m.longitude) - radians(:lng)) + " +
            "sin(radians(:lat)) * sin(radians(m.latitude))))",
            nativeQuery = true)
    List<Mechanic> findNearbyMechanics(@Param("lat") BigDecimal latitude, 
                                      @Param("lng") BigDecimal longitude, 
                                      @Param("radius") double radiusInKm);
    
    /**
     * Find mechanics by hourly rate range
     */
    List<Mechanic> findByHourlyRateBetweenAndIsAvailableTrue(BigDecimal minRate, BigDecimal maxRate);
    
    /**
     * Find mechanics by minimum rating
     */
    List<Mechanic> findByRatingGreaterThanEqualAndIsAvailableTrue(BigDecimal minRating);
}
package com.mechanicondemand.repository;

import com.mechanicondemand.model.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Mechanic Repository - Interface for database operations on Mechanic entity
 */
@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
    
    Optional<Mechanic> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    // Find all available mechanics
    List<Mechanic> findByAvailableTrue();
    
    // Find mechanics by specialty
    List<Mechanic> findBySpecialtyAndAvailableTrue(String specialty);
    
    /**
     * Find nearby mechanics using Haversine formula
     * This calculates distance between two GPS coordinates
     * Returns mechanics within specified radius (in kilometers)
     */
    @Query(value = "SELECT * FROM mechanics m WHERE m.available = true " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(m.latitude)) * " +
           "cos(radians(m.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(m.latitude)))) <= :radius " +
           "ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(m.latitude)) * " +
           "cos(radians(m.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(m.latitude))))",
           nativeQuery = true)
    List<Mechanic> findNearbyMechanics(
        @Param("latitude") Double latitude,
        @Param("longitude") Double longitude,
        @Param("radius") Double radius
    );
}

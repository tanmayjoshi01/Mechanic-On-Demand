package com.mechanicondemand.repository;

import com.mechanicondemand.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Service Repository
 *
 * Provides database operations for Service entities.
 * Extends JpaRepository for standard CRUD operations.
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    /**
     * Find active services
     *
     * @return List of active services
     */
    List<Service> findByIsActiveTrue();

    /**
     * Find services by name (case-insensitive partial match)
     *
     * @param name Service name to search for
     * @return List of services containing the name
     */
    List<Service> findByNameContainingIgnoreCase(String name);

    /**
     * Find services by base price range
     *
     * @param minPrice Minimum base price
     * @param maxPrice Maximum base price
     * @return List of services within price range
     */
    List<Service> findByBasePriceBetween(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice);
}
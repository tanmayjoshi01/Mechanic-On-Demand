package com.mechanicondemand.repository;

import com.mechanicondemand.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Customer Repository
 *
 * Provides database operations for Customer entities.
 * Extends JpaRepository for standard CRUD operations.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find customer by email
     *
     * @param email Customer email
     * @return Optional containing customer if found
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Find customers by city
     *
     * @param city City name
     * @return List of customers in the specified city
     */
    List<Customer> findByCity(String city);

    /**
     * Find nearby customers within radius (using latitude and longitude)
     *
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param radiusKm Radius in kilometers
     * @return List of customers within the radius
     */
    @Query("SELECT c FROM Customer c WHERE c.latitude IS NOT NULL AND c.longitude IS NOT NULL " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(c.latitude)) * " +
           "cos(radians(c.longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(c.latitude)))) <= :radiusKm")
    List<Customer> findNearbyCustomers(@Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude,
                                      @Param("radiusKm") Double radiusKm);
}
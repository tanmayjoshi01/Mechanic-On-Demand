package com.mechanicondemand.repository;

import com.mechanicondemand.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Mechanic Repository
 *
 * Provides database operations for Mechanic entities.
 * Extends JpaRepository for standard CRUD operations.
 */
@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, Long> {

    /**
     * Find mechanic by email
     *
     * @param email Mechanic email
     * @return Optional containing mechanic if found
     */
    Optional<Mechanic> findByEmail(String email);

    /**
     * Find available mechanics
     *
     * @return List of available mechanics
     */
    List<Mechanic> findByIsAvailableTrue();

    /**
     * Find mechanics by rating (greater than or equal to)
     *
     * @param minRating Minimum rating
     * @return List of mechanics with rating >= minRating
     */
    List<Mechanic> findByRatingGreaterThanEqual(Double minRating);

    /**
     * Find nearby mechanics within radius
     *
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param radiusKm Radius in kilometers
     * @return List of mechanics within the radius
     */
    @Query("SELECT m FROM Mechanic m WHERE m.isAvailable = true " +
           "AND m.currentLatitude IS NOT NULL AND m.currentLongitude IS NOT NULL " +
           "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(m.currentLatitude)) * " +
           "cos(radians(m.currentLongitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
           "sin(radians(m.currentLatitude)))) <= :radiusKm " +
           "ORDER BY m.rating DESC")
    List<Mechanic> findNearbyMechanics(@Param("latitude") Double latitude,
                                      @Param("longitude") Double longitude,
                                      @Param("radiusKm") Double radiusKm);

    /**
     * Find mechanics by experience years (greater than or equal to)
     *
     * @param minExperience Minimum experience years
     * @return List of mechanics with experience >= minExperience
     */
    List<Mechanic> findByExperienceYearsGreaterThanEqual(Integer minExperience);

    /**
     * Count total jobs completed by a mechanic
     *
     * @param mechanicId Mechanic ID
     * @return Total jobs completed
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN b.status = 'COMPLETED' THEN 1 ELSE 0 END), 0) " +
           "FROM Booking b WHERE b.mechanic.id = :mechanicId")
    Long countCompletedJobsByMechanic(@Param("mechanicId") Long mechanicId);

    /**
     * Find top rated mechanics
     *
     * @param limit Maximum number of results
     * @return List of top rated mechanics
     */
    @Query("SELECT m FROM Mechanic m WHERE m.isAvailable = true AND m.rating > 0 " +
           "ORDER BY m.rating DESC, m.totalJobsCompleted DESC")
    List<Mechanic> findTopRatedMechanics(@Param("limit") int limit);
}
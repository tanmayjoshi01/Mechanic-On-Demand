package com.mechanicondemand.repository;

import com.mechanicondemand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository - Data Access Layer for User entity
 * 
 * JpaRepository provides:
 * - Basic CRUD operations (save, findById, findAll, delete, etc.)
 * - Pagination and sorting support
 * - Query methods by method names
 * - Custom queries using @Query annotation
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     * Spring Data JPA automatically implements this method based on method name
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by username or email
     */
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    /**
     * Find users by user type (CUSTOMER or MECHANIC)
     */
    List<User> findByUserType(User.UserType userType);
    
    /**
     * Find active users by user type
     */
    List<User> findByUserTypeAndIsActive(User.UserType userType, Boolean isActive);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find mechanics near a location (within specified radius)
     * This is a custom query using native SQL
     */
    @Query(value = "SELECT u.* FROM users u " +
                   "JOIN mechanic_profiles mp ON u.id = mp.user_id " +
                   "WHERE u.user_type = 'MECHANIC' " +
                   "AND u.is_active = true " +
                   "AND mp.is_available = true " +
                   "AND (6371 * acos(cos(radians(:latitude)) * cos(radians(mp.current_latitude)) * " +
                   "cos(radians(mp.current_longitude) - radians(:longitude)) + " +
                   "sin(radians(:latitude)) * sin(radians(mp.current_latitude)))) <= :radius",
           nativeQuery = true)
    List<User> findNearbyMechanics(@Param("latitude") Double latitude, 
                                  @Param("longitude") Double longitude, 
                                  @Param("radius") Double radius);
    
    /**
     * Find mechanics by specialization
     */
    @Query("SELECT u FROM User u JOIN u.mechanicProfile mp WHERE u.userType = 'MECHANIC' " +
           "AND u.isActive = true AND mp.specialization LIKE %:specialization%")
    List<User> findMechanicsBySpecialization(@Param("specialization") String specialization);
}
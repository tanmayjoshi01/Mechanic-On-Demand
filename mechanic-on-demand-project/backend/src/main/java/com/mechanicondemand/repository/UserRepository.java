package com.mechanicondemand.repository;

import com.mechanicondemand.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User Repository
 *
 * Provides database operations for User entities.
 * Extends JpaRepository for standard CRUD operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     *
     * @param email User email
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username
     *
     * @param username User username
     * @return Optional containing user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if email exists
     *
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists
     *
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Find user by email and return UserDetails for Spring Security
     * This query is used by UserDetailsService
     *
     * @param email User email
     * @return Optional containing user if found
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findActiveUserByEmail(String email);
}
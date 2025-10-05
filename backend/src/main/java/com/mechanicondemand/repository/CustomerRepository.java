package com.mechanicondemand.repository;

import com.mechanicondemand.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Customer Repository - Interface for database operations on Customer entity
 * JpaRepository provides built-in methods like save(), findById(), findAll(), delete()
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // Custom query method - Spring Data JPA automatically implements this
    // Finds a customer by email
    Optional<Customer> findByEmail(String email);
    
    // Check if email exists
    boolean existsByEmail(String email);
}

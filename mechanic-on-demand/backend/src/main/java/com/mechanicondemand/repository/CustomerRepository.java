package com.mechanicondemand.repository;

import com.mechanicondemand.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 📊 Customer Repository
 * 
 * This interface extends JpaRepository which provides:
 * - Basic CRUD operations (Create, Read, Update, Delete)
 * - No need to write SQL queries manually
 * - Spring Data JPA generates implementations automatically
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Find customer by email
     * Spring Data JPA automatically creates this method based on the method name
     * @param email Customer's email
     * @return Optional containing customer if found
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Check if customer exists by email
     * @param email Customer's email
     * @return true if customer exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find customer by phone number
     * @param phone Customer's phone
     * @return Optional containing customer if found
     */
    Optional<Customer> findByPhone(String phone);
}
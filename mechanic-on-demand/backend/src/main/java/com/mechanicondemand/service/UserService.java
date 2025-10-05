package com.mechanicondemand.service;

import com.mechanicondemand.dto.RegisterRequest;
import com.mechanicondemand.entity.CustomerProfile;
import com.mechanicondemand.entity.MechanicProfile;
import com.mechanicondemand.entity.User;
import com.mechanicondemand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserService - Business logic layer for User operations
 * 
 * Service Layer Pattern:
 * - Contains business logic
 * - Coordinates between controllers and repositories
 * - Handles transactions
 * - Provides a clean interface for controllers
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     */
    public User registerUser(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setUserType(registerRequest.getUserType());
        user.setIsActive(true);
        
        // Save user
        User savedUser = userRepository.save(user);
        
        // Create profile based on user type
        if (savedUser.getUserType() == User.UserType.CUSTOMER) {
            CustomerProfile customerProfile = new CustomerProfile();
            customerProfile.setUser(savedUser);
            // Set default values or get from request
            savedUser.setCustomerProfile(customerProfile);
        } else if (savedUser.getUserType() == User.UserType.MECHANIC) {
            MechanicProfile mechanicProfile = new MechanicProfile();
            mechanicProfile.setUser(savedUser);
            // Set default values or get from request
            savedUser.setMechanicProfile(mechanicProfile);
        }
        
        return userRepository.save(savedUser);
    }
    
    /**
     * Find user by username or email
     */
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }
    
    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Get all users by type
     */
    public List<User> getUsersByType(User.UserType userType) {
        return userRepository.findByUserTypeAndIsActive(userType, true);
    }
    
    /**
     * Find nearby mechanics
     */
    public List<User> findNearbyMechanics(Double latitude, Double longitude, Double radius) {
        return userRepository.findNearbyMechanics(latitude, longitude, radius);
    }
    
    /**
     * Find mechanics by specialization
     */
    public List<User> findMechanicsBySpecialization(String specialization) {
        return userRepository.findMechanicsBySpecialization(specialization);
    }
    
    /**
     * Update user
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Delete user (soft delete)
     */
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        }
    }
}
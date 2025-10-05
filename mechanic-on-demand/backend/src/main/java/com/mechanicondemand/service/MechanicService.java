package com.mechanicondemand.service;

import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 🔧 Mechanic Service
 * 
 * This class contains all business logic related to mechanics.
 */
@Service
public class MechanicService implements UserDetailsService {
    
    @Autowired
    private MechanicRepository mechanicRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new mechanic
     */
    public Mechanic registerMechanic(Mechanic mechanic) {
        // Check if mechanic already exists
        if (mechanicRepository.existsByEmail(mechanic.getEmail())) {
            throw new RuntimeException("Mechanic with email " + mechanic.getEmail() + " already exists");
        }
        
        // Encrypt password before saving
        mechanic.setPassword(passwordEncoder.encode(mechanic.getPassword()));
        
        // Save mechanic to database
        return mechanicRepository.save(mechanic);
    }
    
    /**
     * Find mechanic by email
     */
    public Optional<Mechanic> findByEmail(String email) {
        return mechanicRepository.findByEmail(email);
    }
    
    /**
     * Find mechanic by ID
     */
    public Optional<Mechanic> findById(Long id) {
        return mechanicRepository.findById(id);
    }
    
    /**
     * Get all mechanics
     */
    public List<Mechanic> getAllMechanics() {
        return mechanicRepository.findAll();
    }
    
    /**
     * Get available mechanics
     */
    public List<Mechanic> getAvailableMechanics() {
        return mechanicRepository.findByIsAvailableTrue();
    }
    
    /**
     * Find nearby mechanics
     */
    public List<Mechanic> findNearbyMechanics(BigDecimal latitude, BigDecimal longitude, double radiusInKm) {
        return mechanicRepository.findNearbyMechanics(latitude, longitude, radiusInKm);
    }
    
    /**
     * Find mechanics by specialization
     */
    public List<Mechanic> findMechanicsBySpecialization(String specialization) {
        return mechanicRepository.findBySpecializationContainingIgnoreCase(specialization);
    }
    
    /**
     * Find mechanics by hourly rate range
     */
    public List<Mechanic> findMechanicsByRateRange(BigDecimal minRate, BigDecimal maxRate) {
        return mechanicRepository.findByHourlyRateBetweenAndIsAvailableTrue(minRate, maxRate);
    }
    
    /**
     * Find mechanics by minimum rating
     */
    public List<Mechanic> findMechanicsByMinRating(BigDecimal minRating) {
        return mechanicRepository.findByRatingGreaterThanEqualAndIsAvailableTrue(minRating);
    }
    
    /**
     * Update mechanic information
     */
    public Mechanic updateMechanic(Mechanic mechanic) {
        return mechanicRepository.save(mechanic);
    }
    
    /**
     * Update mechanic availability
     */
    public Mechanic updateAvailability(Long mechanicId, boolean isAvailable) {
        Optional<Mechanic> mechanicOpt = mechanicRepository.findById(mechanicId);
        if (mechanicOpt.isPresent()) {
            Mechanic mechanic = mechanicOpt.get();
            mechanic.setIsAvailable(isAvailable);
            return mechanicRepository.save(mechanic);
        }
        throw new RuntimeException("Mechanic not found with ID: " + mechanicId);
    }
    
    /**
     * Update mechanic rating
     */
    public Mechanic updateRating(Long mechanicId, int newRating) {
        Optional<Mechanic> mechanicOpt = mechanicRepository.findById(mechanicId);
        if (mechanicOpt.isPresent()) {
            Mechanic mechanic = mechanicOpt.get();
            
            // Calculate new average rating
            int totalRatings = mechanic.getTotalRatings();
            BigDecimal currentRating = mechanic.getRating();
            BigDecimal newAverage = currentRating.multiply(BigDecimal.valueOf(totalRatings))
                    .add(BigDecimal.valueOf(newRating))
                    .divide(BigDecimal.valueOf(totalRatings + 1), 2, BigDecimal.ROUND_HALF_UP);
            
            mechanic.setRating(newAverage);
            mechanic.setTotalRatings(totalRatings + 1);
            
            return mechanicRepository.save(mechanic);
        }
        throw new RuntimeException("Mechanic not found with ID: " + mechanicId);
    }
    
    /**
     * Delete mechanic
     */
    public void deleteMechanic(Long id) {
        mechanicRepository.deleteById(id);
    }
    
    /**
     * Authenticate mechanic (check email and password)
     */
    public Mechanic authenticateMechanic(String email, String password) {
        Optional<Mechanic> mechanicOpt = mechanicRepository.findByEmail(email);
        if (mechanicOpt.isPresent()) {
            Mechanic mechanic = mechanicOpt.get();
            if (passwordEncoder.matches(password, mechanic.getPassword())) {
                return mechanic;
            }
        }
        return null;
    }
    
    /**
     * Required by UserDetailsService for Spring Security
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Mechanic mechanic = mechanicRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Mechanic not found with email: " + email));
        
        return new User(
            mechanic.getEmail(),
            mechanic.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_MECHANIC"))
        );
    }
}
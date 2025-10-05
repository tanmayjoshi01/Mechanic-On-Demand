package com.mechanicondemand.service;

import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Mechanic Service - Handles mechanic-related business logic
 */
@Service
public class MechanicService {
    
    @Autowired
    private MechanicRepository mechanicRepository;
    
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
        return mechanicRepository.findByAvailableTrue();
    }
    
    /**
     * Find nearby mechanics
     * @param latitude Customer's latitude
     * @param longitude Customer's longitude
     * @param radius Search radius in kilometers
     */
    public List<Mechanic> findNearbyMechanics(Double latitude, Double longitude, Double radius) {
        return mechanicRepository.findNearbyMechanics(latitude, longitude, radius);
    }
    
    /**
     * Get mechanic by ID
     */
    public Mechanic getMechanicById(Long id) {
        return mechanicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));
    }
    
    /**
     * Update mechanic availability
     */
    public Mechanic updateAvailability(Long mechanicId, Boolean available) {
        Mechanic mechanic = getMechanicById(mechanicId);
        mechanic.setAvailable(available);
        return mechanicRepository.save(mechanic);
    }
    
    /**
     * Update mechanic profile
     */
    public Mechanic updateMechanic(Long mechanicId, Mechanic updatedMechanic) {
        Mechanic mechanic = getMechanicById(mechanicId);
        
        if (updatedMechanic.getName() != null) {
            mechanic.setName(updatedMechanic.getName());
        }
        if (updatedMechanic.getPhone() != null) {
            mechanic.setPhone(updatedMechanic.getPhone());
        }
        if (updatedMechanic.getAddress() != null) {
            mechanic.setAddress(updatedMechanic.getAddress());
        }
        if (updatedMechanic.getSpecialty() != null) {
            mechanic.setSpecialty(updatedMechanic.getSpecialty());
        }
        if (updatedMechanic.getLatitude() != null) {
            mechanic.setLatitude(updatedMechanic.getLatitude());
        }
        if (updatedMechanic.getLongitude() != null) {
            mechanic.setLongitude(updatedMechanic.getLongitude());
        }
        if (updatedMechanic.getHourlyRate() != null) {
            mechanic.setHourlyRate(updatedMechanic.getHourlyRate());
        }
        if (updatedMechanic.getMonthlySubscription() != null) {
            mechanic.setMonthlySubscription(updatedMechanic.getMonthlySubscription());
        }
        if (updatedMechanic.getYearlySubscription() != null) {
            mechanic.setYearlySubscription(updatedMechanic.getYearlySubscription());
        }
        
        return mechanicRepository.save(mechanic);
    }
}

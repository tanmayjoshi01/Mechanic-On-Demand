package com.mechanicondemand.service;

import com.mechanicondemand.entity.Mechanic;
import com.mechanicondemand.repository.MechanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Mechanic Service
 *
 * Business logic layer for mechanic-related operations.
 * Handles profile management, location updates, and availability management.
 */
@Service
@Transactional
public class MechanicService {

    private final MechanicRepository mechanicRepository;

    @Autowired
    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    /**
     * Get mechanic by email
     *
     * @param email Mechanic email
     * @return Mechanic if found, null otherwise
     */
    public Mechanic getMechanicByEmail(String email) {
        return mechanicRepository.findByEmail(email).orElse(null);
    }

    /**
     * Update mechanic profile
     *
     * @param email Mechanic email
     * @param updatedMechanic Updated mechanic data
     * @return Updated mechanic
     */
    public Mechanic updateMechanicProfile(String email, Mechanic updatedMechanic) {
        Mechanic mechanic = mechanicRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        // Update allowed fields
        mechanic.setFirstName(updatedMechanic.getFirstName());
        mechanic.setLastName(updatedMechanic.getLastName());
        mechanic.setPhoneNumber(updatedMechanic.getPhoneNumber());
        mechanic.setExperienceYears(updatedMechanic.getExperienceYears());
        mechanic.setServiceRadiusKm(updatedMechanic.getServiceRadiusKm());
        mechanic.setLicenseNumber(updatedMechanic.getLicenseNumber());
        mechanic.setSpecializations(updatedMechanic.getSpecializations());

        return mechanicRepository.save(mechanic);
    }

    /**
     * Update mechanic availability
     *
     * @param email Mechanic email
     * @param isAvailable New availability status
     * @return Success message
     */
    public String updateAvailability(String email, Boolean isAvailable) {
        Mechanic mechanic = mechanicRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        mechanic.setIsAvailable(isAvailable);
        mechanicRepository.save(mechanic);

        return isAvailable ? "You are now available for service requests" : "You are now unavailable for service requests";
    }

    /**
     * Get nearby mechanics
     *
     * @param latitude Center latitude
     * @param longitude Center longitude
     * @param radiusKm Search radius in kilometers
     * @return List of nearby available mechanics
     */
    public List<Mechanic> getNearbyMechanics(Double latitude, Double longitude, Double radiusKm) {
        return mechanicRepository.findNearbyMechanics(latitude, longitude, radiusKm);
    }

    /**
     * Update mechanic location
     *
     * @param email Mechanic email
     * @param latitude New latitude
     * @param longitude New longitude
     * @return Success message
     */
    public String updateLocation(String email, Double latitude, Double longitude) {
        Mechanic mechanic = mechanicRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        mechanic.setCurrentLatitude(latitude);
        mechanic.setCurrentLongitude(longitude);
        mechanicRepository.save(mechanic);

        return "Location updated successfully";
    }

    /**
     * Get mechanic's bookings (placeholder implementation)
     *
     * @param email Mechanic email
     * @param status Optional status filter
     * @return List of bookings (to be implemented with Booking entity)
     */
    public List<Object> getMechanicBookings(String email, String status) {
        // Placeholder implementation - will be implemented when booking system is complete
        return List.of(); // Return empty list for now
    }

    /**
     * Get available mechanics
     *
     * @return List of all available mechanics
     */
    public List<Mechanic> getAvailableMechanics() {
        return mechanicRepository.findByIsAvailableTrue();
    }

    /**
     * Get top-rated mechanics
     *
     * @param limit Maximum number of results
     * @return List of top-rated mechanics
     */
    public List<Mechanic> getTopRatedMechanics(int limit) {
        return mechanicRepository.findTopRatedMechanics(limit);
    }
}
package com.mechanicondemand.service;

import com.mechanicondemand.entity.Service;
import com.mechanicondemand.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service Service
 *
 * Business logic layer for service-related operations.
 * Handles service data retrieval and business rules.
 */
@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Get all services
     *
     * @return List of all services
     */
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Get active services only
     *
     * @return List of active services
     */
    public List<Service> getActiveServices() {
        return serviceRepository.findByIsActiveTrue();
    }

    /**
     * Get service by ID
     *
     * @param id Service ID
     * @return Service if found, null otherwise
     */
    public Service getServiceById(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    /**
     * Get services by name (for search functionality)
     *
     * @param name Service name (partial match)
     * @return List of services matching the name
     */
    public List<Service> getServicesByName(String name) {
        return serviceRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Create new service
     *
     * @param service Service to create
     * @return Created service
     */
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    /**
     * Update existing service
     *
     * @param id Service ID
     * @param service Updated service data
     * @return Updated service
     */
    public Service updateService(Long id, Service service) {
        Service existingService = serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setBasePrice(service.getBasePrice());
        existingService.setHourlyRate(service.getHourlyRate());
        existingService.setEstimatedDurationHours(service.getEstimatedDurationHours());
        existingService.setIsActive(service.getIsActive());

        return serviceRepository.save(existingService);
    }
}
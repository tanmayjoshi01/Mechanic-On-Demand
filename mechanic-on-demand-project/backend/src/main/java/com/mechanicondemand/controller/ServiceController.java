package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.entity.Service;
import com.mechanicondemand.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Service Controller
 *
 * Handles service-related operations including:
 * - Getting all available services
 * - Getting specific service details
 *
 * Demonstrates REST API patterns:
 * - GET for retrieving data
 * - Path variables for resource identification
 * - Query parameters for filtering
 * - Proper HTTP status codes
 */
@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    /**
     * Get All Services Endpoint
     *
     * GET /api/services
     *
     * Retrieves all available services.
     * Optional query parameter: active=true to get only active services
     *
     * Query Parameters:
     * - active: boolean (optional) - Filter by active status
     *
     * Response (200 OK):
     * {
     *   "success": true,
     *   "message": "Services retrieved successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "name": "Engine Repair",
     *       "description": "Complete engine diagnostic and repair services",
     *       "basePrice": 150.00,
     *       "hourlyRate": 75.00,
     *       "estimatedDurationHours": 2.0,
     *       "isActive": true,
     *       "createdAt": "2024-01-01T10:00:00"
     *     }
     *   ]
     * }
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Service>>> getAllServices(
            @RequestParam(value = "active", required = false) Boolean active) {
        try {
            List<Service> services;

            if (active != null) {
                services = serviceService.getActiveServices();
            } else {
                services = serviceService.getAllServices();
            }

            ApiResponse<List<Service>> response = ApiResponse.success("Services retrieved successfully", services);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Service>> response = ApiResponse.error("Failed to retrieve services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get Service by ID Endpoint
     *
     * GET /api/services/{id}
     *
     * Retrieves a specific service by its ID.
     *
     * Path Parameters:
     * - id: Long - The service ID
     *
     * Response (200 OK):
     * {
     *   "success": true,
     *   "message": "Service retrieved successfully",
     *   "data": {
     *     "id": 1,
     *     "name": "Engine Repair",
     *     "description": "Complete engine diagnostic and repair services",
     *     "basePrice": 150.00,
     *     "hourlyRate": 75.00,
     *     "estimatedDurationHours": 2.0,
     *     "isActive": true,
     *     "createdAt": "2024-01-01T10:00:00"
     *   }
     * }
     *
     * Response (404 Not Found):
     * {
     *   "success": false,
     *   "message": "Service not found with id: 999",
     *   "errorCode": "SERVICE_NOT_FOUND"
     * }
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Service>> getServiceById(@PathVariable Long id) {
        try {
            Service service = serviceService.getServiceById(id);

            if (service != null) {
                ApiResponse<Service> response = ApiResponse.success("Service retrieved successfully", service);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<Service> response = ApiResponse.error("Service not found with id: " + id, "SERVICE_NOT_FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            ApiResponse<Service> response = ApiResponse.error("Failed to retrieve service: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
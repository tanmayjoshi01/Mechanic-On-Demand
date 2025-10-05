package com.mechanicondemand.controller;

import com.mechanicondemand.model.Customer;
import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.security.JwtUtil;
import com.mechanicondemand.service.CustomerService;
import com.mechanicondemand.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 🔐 Authentication Controller
 * 
 * This controller handles all authentication-related endpoints:
 * - Customer registration and login
 * - Mechanic registration and login
 * - JWT token generation
 * 
 * REST API Explanation:
 * - REST (Representational State Transfer) is an architectural style for designing web services
 * - It uses HTTP methods: GET, POST, PUT, DELETE
 * - Data is exchanged in JSON format
 * - URLs represent resources (e.g., /api/auth/customer/register)
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private MechanicService mechanicService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Register a new customer
     * POST /api/auth/customer/register
     */
    @PostMapping("/customer/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody Customer customer) {
        try {
            Customer savedCustomer = customerService.registerCustomer(customer);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedCustomer.getEmail(), savedCustomer.getId(), "CUSTOMER");
            
            // Return response without password
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Customer registered successfully");
            response.put("token", token);
            response.put("customer", Map.of(
                "id", savedCustomer.getId(),
                "firstName", savedCustomer.getFirstName(),
                "lastName", savedCustomer.getLastName(),
                "email", savedCustomer.getEmail(),
                "phone", savedCustomer.getPhone()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Login customer
     * POST /api/auth/customer/login
     */
    @PostMapping("/customer/login")
    public ResponseEntity<?> loginCustomer(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            Customer customer = customerService.authenticateCustomer(email, password);
            if (customer != null) {
                // Generate JWT token
                String token = jwtUtil.generateToken(customer.getEmail(), customer.getId(), "CUSTOMER");
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("customer", Map.of(
                    "id", customer.getId(),
                    "firstName", customer.getFirstName(),
                    "lastName", customer.getLastName(),
                    "email", customer.getEmail(),
                    "phone", customer.getPhone()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid email or password");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Register a new mechanic
     * POST /api/auth/mechanic/register
     */
    @PostMapping("/mechanic/register")
    public ResponseEntity<?> registerMechanic(@Valid @RequestBody Mechanic mechanic) {
        try {
            Mechanic savedMechanic = mechanicService.registerMechanic(mechanic);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(savedMechanic.getEmail(), savedMechanic.getId(), "MECHANIC");
            
            // Return response without password
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Mechanic registered successfully");
            response.put("token", token);
            response.put("mechanic", Map.of(
                "id", savedMechanic.getId(),
                "firstName", savedMechanic.getFirstName(),
                "lastName", savedMechanic.getLastName(),
                "email", savedMechanic.getEmail(),
                "phone", savedMechanic.getPhone(),
                "specialization", savedMechanic.getSpecialization(),
                "hourlyRate", savedMechanic.getHourlyRate()
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Login mechanic
     * POST /api/auth/mechanic/login
     */
    @PostMapping("/mechanic/login")
    public ResponseEntity<?> loginMechanic(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String password = loginRequest.get("password");
            
            Mechanic mechanic = mechanicService.authenticateMechanic(email, password);
            if (mechanic != null) {
                // Generate JWT token
                String token = jwtUtil.generateToken(mechanic.getEmail(), mechanic.getId(), "MECHANIC");
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("token", token);
                response.put("mechanic", Map.of(
                    "id", mechanic.getId(),
                    "firstName", mechanic.getFirstName(),
                    "lastName", mechanic.getLastName(),
                    "email", mechanic.getEmail(),
                    "phone", mechanic.getPhone(),
                    "specialization", mechanic.getSpecialization(),
                    "hourlyRate", mechanic.getHourlyRate(),
                    "rating", mechanic.getRating(),
                    "isAvailable", mechanic.getIsAvailable()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid email or password");
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
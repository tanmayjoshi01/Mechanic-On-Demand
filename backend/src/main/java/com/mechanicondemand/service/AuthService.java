package com.mechanicondemand.service;

import com.mechanicondemand.dto.AuthResponse;
import com.mechanicondemand.dto.LoginRequest;
import com.mechanicondemand.dto.RegisterRequest;
import com.mechanicondemand.model.Customer;
import com.mechanicondemand.model.Mechanic;
import com.mechanicondemand.repository.CustomerRepository;
import com.mechanicondemand.repository.MechanicRepository;
import com.mechanicondemand.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service - Handles user registration and login
 */
@Service
public class AuthService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private MechanicRepository mechanicRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Register a new user (Customer or Mechanic)
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (customerRepository.existsByEmail(request.getEmail()) || 
            mechanicRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Register based on role
        if ("CUSTOMER".equalsIgnoreCase(request.getRole())) {
            return registerCustomer(request);
        } else if ("MECHANIC".equalsIgnoreCase(request.getRole())) {
            return registerMechanic(request);
        } else {
            throw new RuntimeException("Invalid role. Must be CUSTOMER or MECHANIC");
        }
    }
    
    /**
     * Register a customer
     */
    private AuthResponse registerCustomer(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        customer.setLatitude(request.getLatitude());
        customer.setLongitude(request.getLongitude());
        customer.setRole("CUSTOMER");
        
        Customer savedCustomer = customerRepository.save(customer);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedCustomer.getEmail(), savedCustomer.getRole());
        
        return new AuthResponse(token, savedCustomer.getId(), savedCustomer.getName(), 
                               savedCustomer.getEmail(), savedCustomer.getRole());
    }
    
    /**
     * Register a mechanic
     */
    private AuthResponse registerMechanic(RegisterRequest request) {
        Mechanic mechanic = new Mechanic();
        mechanic.setName(request.getName());
        mechanic.setEmail(request.getEmail());
        mechanic.setPassword(passwordEncoder.encode(request.getPassword()));
        mechanic.setPhone(request.getPhone());
        mechanic.setAddress(request.getAddress());
        mechanic.setLatitude(request.getLatitude());
        mechanic.setLongitude(request.getLongitude());
        mechanic.setSpecialty(request.getSpecialty() != null ? request.getSpecialty() : "All");
        mechanic.setRole("MECHANIC");
        
        // Set pricing
        if (request.getHourlyRate() != null) {
            mechanic.setHourlyRate(request.getHourlyRate());
        }
        if (request.getMonthlySubscription() != null) {
            mechanic.setMonthlySubscription(request.getMonthlySubscription());
        }
        if (request.getYearlySubscription() != null) {
            mechanic.setYearlySubscription(request.getYearlySubscription());
        }
        
        Mechanic savedMechanic = mechanicRepository.save(mechanic);
        
        // Generate JWT token
        String token = jwtUtil.generateToken(savedMechanic.getEmail(), savedMechanic.getRole());
        
        return new AuthResponse(token, savedMechanic.getId(), savedMechanic.getName(), 
                               savedMechanic.getEmail(), savedMechanic.getRole());
    }
    
    /**
     * Login user
     */
    public AuthResponse login(LoginRequest request) {
        // Try to find customer
        Customer customer = customerRepository.findByEmail(request.getEmail()).orElse(null);
        if (customer != null) {
            if (passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                String token = jwtUtil.generateToken(customer.getEmail(), customer.getRole());
                return new AuthResponse(token, customer.getId(), customer.getName(), 
                                       customer.getEmail(), customer.getRole());
            }
        }
        
        // Try to find mechanic
        Mechanic mechanic = mechanicRepository.findByEmail(request.getEmail()).orElse(null);
        if (mechanic != null) {
            if (passwordEncoder.matches(request.getPassword(), mechanic.getPassword())) {
                String token = jwtUtil.generateToken(mechanic.getEmail(), mechanic.getRole());
                return new AuthResponse(token, mechanic.getId(), mechanic.getName(), 
                                       mechanic.getEmail(), mechanic.getRole());
            }
        }
        
        throw new RuntimeException("Invalid email or password");
    }
}

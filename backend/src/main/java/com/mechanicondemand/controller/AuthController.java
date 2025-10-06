package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.dto.AuthResponse;
import com.mechanicondemand.dto.LoginRequest;
import com.mechanicondemand.dto.RegisterRequest;
import com.mechanicondemand.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller - Handles registration and login endpoints
 * 
 * REST API Endpoints:
 * POST /api/auth/register - Register new user
 * POST /api/auth/login - Login user
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")  // Allow requests from any origin (frontend)
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * POST /api/auth/register
     * Register a new customer or mechanic
     * 
     * HTTP Method: POST - Used to create new resources
     * Request Body: JSON with user details
     * Response: JWT token and user information
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(new ApiResponse(true, "Registration successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
    
    /**
     * POST /api/auth/login
     * Login existing user
     * 
     * HTTP Method: POST - Used to submit credentials
     * Request Body: JSON with email and password
     * Response: JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(new ApiResponse(true, "Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, e.getMessage()));
        }
    }
}

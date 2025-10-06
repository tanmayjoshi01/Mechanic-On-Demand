package com.mechanicondemand.controller;

import com.mechanicondemand.dto.ApiResponse;
import com.mechanicondemand.dto.LoginRequest;
import com.mechanicondemand.dto.LoginResponse;
import com.mechanicondemand.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 *
 * Handles user authentication operations including:
 * - User registration (customers and mechanics)
 * - User login
 * - User logout
 *
 * This controller demonstrates REST API patterns:
 * - POST for creating/authenticating resources
 * - Proper HTTP status codes
 * - Request validation
 * - Structured response format
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Configure CORS for frontend integration
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User Login Endpoint
     *
     * POST /api/auth/login
     *
     * Authenticates a user and returns a JWT token.
     *
     * Request Body:
     * {
     *   "email": "user@example.com",
     *   "password": "password123"
     * }
     *
     * Response (200 OK):
     * {
     *   "success": true,
     *   "message": "Login successful",
     *   "data": {
     *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *     "tokenType": "Bearer",
     *     "expiresIn": 86400000,
     *     "userId": 1,
     *     "username": "johndoe",
     *     "email": "user@example.com",
     *     "firstName": "John",
     *     "lastName": "Doe",
     *     "userType": "CUSTOMER"
     *   }
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            ApiResponse<LoginResponse> response = ApiResponse.success("Login successful", loginResponse);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<LoginResponse> response = ApiResponse.error("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * User Registration Endpoint (Customer)
     *
     * POST /api/auth/register/customer
     *
     * Creates a new customer account.
     */
    @PostMapping("/register/customer")
    public ResponseEntity<ApiResponse<String>> registerCustomer(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            String message = authService.registerCustomer(registerRequest);
            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * User Registration Endpoint (Mechanic)
     *
     * POST /api/auth/register/mechanic
     *
     * Creates a new mechanic account.
     */
    @PostMapping("/register/mechanic")
    public ResponseEntity<ApiResponse<String>> registerMechanic(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            String message = authService.registerMechanic(registerRequest);
            ApiResponse<String> response = ApiResponse.success(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * User Logout Endpoint
     *
     * POST /api/auth/logout
     *
     * Logs out the current user (mainly for token blacklisting in future).
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        // For now, just return success. In a real application, you might blacklist the token
        ApiResponse<String> response = ApiResponse.success("Logout successful");
        return ResponseEntity.ok(response);
    }
}
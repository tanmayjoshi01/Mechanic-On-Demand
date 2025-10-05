package com.mechanicondemand.controller;

import com.mechanicondemand.dto.AuthResponse;
import com.mechanicondemand.dto.LoginRequest;
import com.mechanicondemand.dto.RegisterRequest;
import com.mechanicondemand.entity.User;
import com.mechanicondemand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthController - REST Controller for authentication endpoints
 * 
 * REST API Principles:
 * - Stateless: Each request contains all necessary information
 * - Resource-based URLs: /api/auth/login, /api/auth/register
 * - HTTP Methods: POST for creating/authenticating
 * - Status Codes: 200 (OK), 201 (Created), 400 (Bad Request), 401 (Unauthorized)
 * - JSON format for data exchange
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private com.mechanicondemand.security.JwtUtils jwtUtils;
    
    /**
     * POST /api/auth/login
     * Authenticate user and return JWT token
     * 
     * HTTP Method: POST
     * Purpose: User login
     * Request Body: LoginRequest (username/email, password)
     * Response: AuthResponse (JWT token + user info)
     * Status Codes: 200 (success), 401 (invalid credentials)
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            // Get user details
            User user = (User) authentication.getPrincipal();
            
            // Create response
            AuthResponse response = new AuthResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserType().toString()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * POST /api/auth/register
     * Register a new user
     * 
     * HTTP Method: POST
     * Purpose: User registration
     * Request Body: RegisterRequest (user details)
     * Response: Success message or error
     * Status Codes: 201 (created), 400 (bad request)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Register user
            User user = userService.registerUser(registerRequest);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("userId", user.getId().toString());
            
            return ResponseEntity.status(201).body(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * GET /api/auth/me
     * Get current user information
     * 
     * HTTP Method: GET
     * Purpose: Get authenticated user details
     * Headers: Authorization: Bearer <token>
     * Response: User information
     * Status Codes: 200 (success), 401 (unauthorized)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not authenticated");
            return ResponseEntity.status(401).body(error);
        }
        
        User user = (User) authentication.getPrincipal();
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("userType", user.getUserType());
        response.put("isActive", user.getIsActive());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * POST /api/auth/logout
     * Logout user (client-side token removal)
     * 
     * HTTP Method: POST
     * Purpose: User logout
     * Response: Success message
     * Status Codes: 200 (success)
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User logged out successfully");
        return ResponseEntity.ok(response);
    }
}
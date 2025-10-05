package com.mechanicondemand.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for authentication response
 * Contains JWT token and user information
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private String role;
    
    public AuthResponse(String token, Long id, String name, String email, String role) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}

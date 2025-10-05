package com.mechanicondemand.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) for registration requests
 * Used to receive data from frontend
 */
@Data
public class RegisterRequest {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    @NotBlank(message = "Phone is required")
    private String phone;
    
    private String address;
    
    private Double latitude;
    
    private Double longitude;
    
    // "CUSTOMER" or "MECHANIC"
    @NotBlank(message = "Role is required")
    private String role;
    
    // For mechanics only
    private String specialty;
    private Double hourlyRate;
    private Double monthlySubscription;
    private Double yearlySubscription;
}

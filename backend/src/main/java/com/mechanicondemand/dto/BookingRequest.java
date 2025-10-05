package com.mechanicondemand.dto;

import com.mechanicondemand.model.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO for creating a new booking
 */
@Data
public class BookingRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Mechanic ID is required")
    private Long mechanicId;
    
    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;
    
    @NotBlank(message = "Vehicle model is required")
    private String vehicleModel;
    
    @NotBlank(message = "Problem description is required")
    private String problemDescription;
    
    private String location;
    
    private Double latitude;
    
    private Double longitude;
    
    @NotNull(message = "Subscription type is required")
    private SubscriptionType subscriptionType;
    
    private LocalDateTime scheduledTime;
}

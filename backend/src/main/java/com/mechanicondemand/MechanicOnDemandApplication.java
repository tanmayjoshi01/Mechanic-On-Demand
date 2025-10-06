package com.mechanicondemand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class
 * 
 * @SpringBootApplication annotation enables:
 * - Auto-configuration: Automatically configures Spring based on dependencies
 * - Component scanning: Finds and registers beans (@Controller, @Service, etc.)
 * - Spring Boot configuration
 */
@SpringBootApplication
public class MechanicOnDemandApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MechanicOnDemandApplication.class, args);
        System.out.println("\n===========================================");
        System.out.println("🚗 Mechanic On Demand API Started!");
        System.out.println("📍 Server running at: http://localhost:8080/api");
        System.out.println("===========================================\n");
    }
}

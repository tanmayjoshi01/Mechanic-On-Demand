package com.mechanicondemand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Class
 *
 * This is the entry point for the Mechanic On Demand backend application.
 * The @SpringBootApplication annotation enables:
 * - Auto-configuration
 * - Component scanning
 * - Configuration properties
 */
@SpringBootApplication
public class MechanicOnDemandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MechanicOnDemandApplication.class, args);
    }
}
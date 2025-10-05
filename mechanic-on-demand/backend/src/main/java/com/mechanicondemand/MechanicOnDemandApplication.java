package com.mechanicondemand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Class
 * 
 * @SpringBootApplication annotation combines:
 * - @Configuration: Marks this class as a configuration class
 * - @EnableAutoConfiguration: Enables Spring Boot auto-configuration
 * - @ComponentScan: Scans for components in the package and sub-packages
 */
@SpringBootApplication
public class MechanicOnDemandApplication {

    /**
     * Main method - Entry point of the application
     * SpringApplication.run() starts the Spring Boot application
     */
    public static void main(String[] args) {
        SpringApplication.run(MechanicOnDemandApplication.class, args);
        System.out.println("🔧 Mechanic On Demand Application Started Successfully!");
        System.out.println("🌐 Access the application at: http://localhost:8080");
    }
}
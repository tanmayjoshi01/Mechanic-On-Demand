package com.mechanicondemand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🚀 Main Spring Boot Application Class
 * 
 * This is the entry point of our application.
 * @SpringBootApplication combines three annotations:
 * - @Configuration: Marks this as a configuration class
 * - @EnableAutoConfiguration: Automatically configures Spring Boot
 * - @ComponentScan: Scans for components in this package and sub-packages
 */
@SpringBootApplication
public class MechanicOnDemandApplication {

    public static void main(String[] args) {
        // This starts the Spring Boot application
        SpringApplication.run(MechanicOnDemandApplication.class, args);
        System.out.println("🔧 Mechanic On Demand Application Started Successfully!");
    }
}
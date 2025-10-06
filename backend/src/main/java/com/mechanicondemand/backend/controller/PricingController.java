package com.mechanicondemand.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> pricing() {
        return ResponseEntity.ok(Map.of(
                "monthly", 29.99,
                "yearly", 299.0,
                "currency", "USD",
                "features", new String[]{"Priority support", "Free towing up to 10km", "Discounted labor"}
        ));
    }
}

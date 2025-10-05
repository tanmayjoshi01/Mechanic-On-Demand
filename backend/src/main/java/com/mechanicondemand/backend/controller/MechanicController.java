package com.mechanicondemand.backend.controller;

import com.mechanicondemand.backend.model.User;
import com.mechanicondemand.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {
    private final UserRepository userRepository;

    public MechanicController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> listMechanics() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.MECHANIC)
                .toList());
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<User>> nearby(@RequestParam double lat, @RequestParam double lng, @RequestParam(defaultValue = "10") double radiusKm) {
        // naive filter by rough bounding box for demo purposes
        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / 111.0;
        return ResponseEntity.ok(userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.MECHANIC)
                .filter(u -> u.getLatitude() != null && u.getLongitude() != null)
                .filter(u -> Math.abs(u.getLatitude() - lat) <= latDelta && Math.abs(u.getLongitude() - lng) <= lngDelta)
                .toList());
    }

    @PutMapping("/me/location")
    @PreAuthorize("hasRole('MECHANIC')")
    public ResponseEntity<Map<String, Object>> updateLocation(Authentication authentication,
                                                             @RequestBody Map<String, Double> body) {
        Long id = Long.parseLong(authentication.getName());
        User mech = userRepository.findById(id).orElseThrow();
        mech.setLatitude(body.get("latitude"));
        mech.setLongitude(body.get("longitude"));
        userRepository.save(mech);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}

package com.mechanicondemand.backend.service;

import com.mechanicondemand.backend.dto.AuthDtos;
import com.mechanicondemand.backend.model.MechanicProfile;
import com.mechanicondemand.backend.model.User;
import com.mechanicondemand.backend.repository.MechanicProfileRepository;
import com.mechanicondemand.backend.repository.UserRepository;
import com.mechanicondemand.backend.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final MechanicProfileRepository mechanicProfileRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository,
                       MechanicProfileRepository mechanicProfileRepository,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.mechanicProfileRepository = mechanicProfileRepository;
        this.jwtService = jwtService;
    }

    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = new User();
        user.setFullName(request.fullName);
        user.setEmail(request.email);
        user.setPhone(request.phone);
        user.setRole(User.Role.valueOf(request.role.toUpperCase()));
        user.setPasswordHash(passwordEncoder.encode(request.password));
        user = userRepository.save(user);

        if (user.getRole() == User.Role.MECHANIC) {
            MechanicProfile profile = new MechanicProfile();
            profile.setUser(user);
            mechanicProfileRepository.save(profile);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        String token = jwtService.generateToken(user.getId().toString(), claims);

        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.token = token;
        response.userId = user.getId();
        response.role = user.getRole().name();
        response.fullName = user.getFullName();
        return response;
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        String token = jwtService.generateToken(user.getId().toString(), claims);

        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.token = token;
        response.userId = user.getId();
        response.role = user.getRole().name();
        response.fullName = user.getFullName();
        return response;
    }
}

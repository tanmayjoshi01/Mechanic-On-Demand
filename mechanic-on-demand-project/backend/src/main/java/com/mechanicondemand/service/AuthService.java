package com.mechanicondemand.service;

import com.mechanicondemand.dto.LoginRequest;
import com.mechanicondemand.dto.LoginResponse;
import com.mechanicondemand.dto.RegisterRequest;
import com.mechanicondemand.entity.Customer;
import com.mechanicondemand.entity.Mechanic;
import com.mechanicondemand.entity.User;
import com.mechanicondemand.repository.CustomerRepository;
import com.mechanicondemand.repository.MechanicRepository;
import com.mechanicondemand.repository.UserRepository;
import com.mechanicondemand.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Authentication Service
 *
 * Handles user authentication operations including:
 * - User registration for customers and mechanics
 * - User login with JWT token generation
 * - Password encoding and validation
 */
@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final MechanicRepository mechanicRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository,
                      CustomerRepository customerRepository,
                      MechanicRepository mechanicRepository,
                      PasswordEncoder passwordEncoder,
                      JwtTokenProvider jwtTokenProvider,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.mechanicRepository = mechanicRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticate user and generate JWT token
     *
     * @param loginRequest Login credentials
     * @return LoginResponse with JWT token and user info
     * @throws Exception if authentication fails
     */
    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        try {
            // Authenticate user with Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), // Using email as username
                    loginRequest.getPassword()
                )
            );

            // Get user details from authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

            // Generate JWT token
            String token = jwtTokenProvider.generateToken(userDetails);

            // Create response
            return new LoginResponse(
                token,
                jwtTokenProvider.getExpirationTime(),
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserType()
            );

        } catch (Exception e) {
            throw new Exception("Invalid email or password");
        }
    }

    /**
     * Register a new customer
     *
     * @param registerRequest Registration data
     * @return Success message
     * @throws Exception if registration fails
     */
    public String registerCustomer(RegisterRequest registerRequest) throws Exception {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new Exception("Email already exists");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setUsername(registerRequest.getUsername());
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setFirstName(registerRequest.getFirstName());
        customer.setLastName(registerRequest.getLastName());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());

        // Save customer
        customerRepository.save(customer);

        return "Customer registered successfully";
    }

    /**
     * Register a new mechanic
     *
     * @param registerRequest Registration data
     * @return Success message
     * @throws Exception if registration fails
     */
    public String registerMechanic(RegisterRequest registerRequest) throws Exception {
        // Check if user already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new Exception("Email already exists");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new Exception("Username already exists");
        }

        // Create new mechanic
        Mechanic mechanic = new Mechanic();
        mechanic.setUsername(registerRequest.getUsername());
        mechanic.setEmail(registerRequest.getEmail());
        mechanic.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        mechanic.setFirstName(registerRequest.getFirstName());
        mechanic.setLastName(registerRequest.getLastName());
        mechanic.setPhoneNumber(registerRequest.getPhoneNumber());

        // Save mechanic
        mechanicRepository.save(mechanic);

        return "Mechanic registered successfully";
    }

    /**
     * Find user by email
     *
     * @param email User email
     * @return Optional containing user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find user by ID
     *
     * @param id User ID
     * @return Optional containing user if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
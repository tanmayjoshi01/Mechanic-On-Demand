package com.mechanicondemand.security;

import com.mechanicondemand.entity.User;
import com.mechanicondemand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom User Details Service
 *
 * Implements Spring Security's UserDetailsService to load user-specific data.
 * Used during authentication process to verify user credentials.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by username (email)
     *
     * This method is called by Spring Security during authentication.
     * It retrieves the user from the database and returns UserDetails.
     *
     * @param username User's email address
     * @return UserDetails object containing user information
     * @throws UsernameNotFoundException if user is not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findActiveUserByEmail(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("User not found with email : " + username)
                );

        return user; // User entity implements UserDetails
    }
}
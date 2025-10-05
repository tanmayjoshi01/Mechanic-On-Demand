package com.mechanicondemand.service;

import com.mechanicondemand.entity.User;
import com.mechanicondemand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserDetailsServiceImpl - Spring Security User Details Service
 * 
 * This class implements UserDetailsService interface to:
 * - Load user details for authentication
 * - Convert User entity to UserDetails for Spring Security
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        
        return UserPrincipal.create(user);
    }
    
    /**
     * UserPrincipal - Custom UserDetails implementation
     * 
     * This class wraps our User entity and implements UserDetails interface
     * required by Spring Security for authentication.
     */
    public static class UserPrincipal implements UserDetails {
        
        private Long id;
        private String username;
        private String email;
        private String password;
        private boolean isActive;
        
        public UserPrincipal(Long id, String username, String email, String password, boolean isActive) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.isActive = isActive;
        }
        
        public static UserPrincipal create(User user) {
            return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getIsActive()
            );
        }
        
        // Getters
        public Long getId() {
            return id;
        }
        
        @Override
        public String getUsername() {
            return username;
        }
        
        public String getEmail() {
            return email;
        }
        
        @Override
        public String getPassword() {
            return password;
        }
        
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
        
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        
        @Override
        public boolean isEnabled() {
            return isActive;
        }
        
        @Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return java.util.Collections.emptyList();
        }
    }
}
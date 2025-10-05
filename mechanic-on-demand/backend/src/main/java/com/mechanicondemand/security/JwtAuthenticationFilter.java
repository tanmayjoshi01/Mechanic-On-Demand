package com.mechanicondemand.security;

import com.mechanicondemand.service.CustomerService;
import com.mechanicondemand.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 🔍 JWT Authentication Filter
 * 
 * This filter runs before every request to check if the user has a valid JWT token.
 * It's like a security guard that checks your ID before letting you in.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private MechanicService mechanicService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // Get the Authorization header from the request
        final String authorizationHeader = request.getHeader("Authorization");
        
        String email = null;
        String jwt = null;
        
        // Extract JWT token from "Bearer <token>" format
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                logger.error("Cannot extract email from JWT token", e);
            }
        }
        
        // If we have email and no current authentication
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Get user type from token
            String userType = jwtUtil.extractUserType(jwt);
            UserDetails userDetails = null;
            
            // Load user details based on user type
            if ("CUSTOMER".equals(userType)) {
                userDetails = customerService.loadUserByUsername(email);
            } else if ("MECHANIC".equals(userType)) {
                userDetails = mechanicService.loadUserByUsername(email);
            }
            
            // Validate token and set authentication
            if (userDetails != null && jwtUtil.validateToken(jwt, email)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
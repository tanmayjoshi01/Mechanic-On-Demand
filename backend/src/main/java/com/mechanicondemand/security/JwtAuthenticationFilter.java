package com.mechanicondemand.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT Authentication Filter
 * 
 * This filter intercepts every HTTP request and checks for JWT token
 * If valid token is found, it authenticates the user
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) throws ServletException, IOException {
        
        // Get Authorization header
        String authHeader = request.getHeader("Authorization");
        
        String token = null;
        String email = null;
        
        // Check if Authorization header contains Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Remove "Bearer " prefix
            try {
                email = jwtUtil.extractEmail(token);
            } catch (Exception e) {
                logger.error("Unable to extract email from token");
            }
        }
        
        // If token is valid and user is not already authenticated
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token)) {
                String role = jwtUtil.extractRole(token);
                
                // Create authentication object
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        email, 
                        null, 
                        Collections.singletonList(new SimpleGrantedAuthority(role))
                    );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}

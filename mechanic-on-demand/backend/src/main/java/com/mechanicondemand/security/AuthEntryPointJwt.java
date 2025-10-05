package com.mechanicondemand.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthEntryPointJwt - Handles unauthorized access attempts
 * 
 * This class is called when a user tries to access a protected resource
 * without proper authentication (invalid or missing JWT token).
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        logger.error("Unauthorized error: {}", authException.getMessage());
        
        // Set response status and content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Create error response
        String errorResponse = String.format(
            "{\"error\": \"Unauthorized: %s\", \"status\": %d, \"path\": \"%s\"}",
            authException.getMessage(),
            HttpServletResponse.SC_UNAUTHORIZED,
            request.getRequestURI()
        );
        
        // Write error response
        response.getWriter().write(errorResponse);
    }
}
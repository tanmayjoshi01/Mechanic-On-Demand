package com.mechanicondemand.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Authentication Entry Point
 *
 * Handles unauthorized access attempts by returning a proper error response
 * instead of redirecting to a login page (since this is a REST API).
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", "Unauthorized: Authentication token was either missing or invalid");
        error.put("errorCode", "UNAUTHORIZED");
        error.put("timestamp", System.currentTimeMillis());

        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().println(mapper.writeValueAsString(error));
    }
}
package com.mechanicondemand.backend.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOriginsCsv;

    @Value("${app.cors.allowed-methods:*}")
    private String allowedMethodsCsv;

    @Value("${app.cors.allowed-headers:*}")
    private String allowedHeadersCsv;

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = Arrays.stream(allowedOriginsCsv.split(","))
            .map(String::trim)
            .toList();
        List<String> allowedMethods = Arrays.stream(allowedMethodsCsv.split(","))
            .map(String::trim)
            .toList();
        List<String> allowedHeaders = Arrays.stream(allowedHeadersCsv.split(","))
            .map(String::trim)
            .toList();

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowedHeaders(allowedHeaders);
        configuration.setAllowCredentials(allowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

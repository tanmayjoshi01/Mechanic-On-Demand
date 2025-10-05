package com.mechanicondemand.config;

import com.mechanicondemand.security.CustomUserDetailsService;
import com.mechanicondemand.security.JwtAuthenticationEntryPoint;
import com.mechanicondemand.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration
 *
 * Configures Spring Security for the application including:
 * - Authentication manager setup
 * - Password encoding
 * - JWT authentication filter
 * - CORS configuration
 * - Authorization rules
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                         JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                         JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Password encoder bean
     *
     * Uses BCrypt for secure password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Configure authentication manager
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    /**
     * Configure HTTP security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                // Public endpoints
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/services/**").permitAll()
                .antMatchers("/h2-console/**").permitAll() // For H2 database console in development
                // Protected endpoints
                .antMatchers("/api/customers/**").hasRole("CUSTOMER")
                .antMatchers("/api/mechanics/**").hasRole("MECHANIC")
                .antMatchers("/api/bookings/**").hasAnyRole("CUSTOMER", "MECHANIC")
                .antMatchers("/api/notifications/**").hasAnyRole("CUSTOMER", "MECHANIC")
                // All other requests require authentication
                .anyRequest().authenticated();

        // Add JWT authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // For H2 database console (development only)
        http.headers().frameOptions().disable();
    }
}
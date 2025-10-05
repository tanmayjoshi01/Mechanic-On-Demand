package com.mechanicondemand.dto;

import com.mechanicondemand.entity.UserType;

/**
 * Login Response DTO
 *
 * Contains the response data after successful authentication,
 * including JWT token and user information.
 */
public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn; // Token expiration time in milliseconds
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private UserType userType;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, Long expiresIn, Long userId, String username,
                        String email, String firstName, String lastName, UserType userType) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
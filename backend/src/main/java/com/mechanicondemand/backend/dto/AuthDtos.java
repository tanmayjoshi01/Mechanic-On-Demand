package com.mechanicondemand.backend.dto;

public class AuthDtos {
    public static class RegisterRequest {
        public String fullName;
        public String email;
        public String phone;
        public String password;
        public String role; // CUSTOMER or MECHANIC
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public Long userId;
        public String role;
        public String fullName;
    }
}

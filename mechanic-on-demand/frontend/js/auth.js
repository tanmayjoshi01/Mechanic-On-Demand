/**
 * 🔐 Authentication JavaScript
 * 
 * This file handles all authentication-related functionality including
 * login, registration, and user session management.
 */

// Authentication state
const AuthState = {
    isAuthenticated: false,
    currentUser: null,
    userType: null,
    token: null
};

// Initialize authentication when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    initializeAuth();
    setupEventListeners();
});

// Initialize authentication
function initializeAuth() {
    // Check if user is already logged in
    const token = localStorage.getItem('token');
    const userType = localStorage.getItem('userType');
    const user = localStorage.getItem('currentUser');

    if (token && userType && user) {
        AuthState.isAuthenticated = true;
        AuthState.token = token;
        AuthState.userType = userType;
        AuthState.currentUser = JSON.parse(user);
        
        // Redirect to appropriate dashboard
        redirectToDashboard();
    }
}

// Setup event listeners
function setupEventListeners() {
    // Login form submission
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }

    // Registration form submission
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegistration);
    }

    // User type selection
    const userTypeRadios = document.querySelectorAll('input[name="userType"]');
    userTypeRadios.forEach(radio => {
        radio.addEventListener('change', handleUserTypeChange);
    });

    // Password visibility toggle
    const togglePasswordBtn = document.getElementById('togglePassword');
    if (togglePasswordBtn) {
        togglePasswordBtn.addEventListener('click', togglePasswordVisibility);
    }

    // Confirm password validation
    const confirmPasswordInput = document.getElementById('confirmPassword');
    if (confirmPasswordInput) {
        confirmPasswordInput.addEventListener('input', validatePasswordMatch);
    }
}

// Handle login form submission
async function handleLogin(event) {
    event.preventDefault();
    
    const submitBtn = document.getElementById('loginBtn');
    const spinner = document.getElementById('loginSpinner');
    const icon = document.getElementById('loginIcon');
    
    // Show loading state
    Utils.showLoading('loginSpinner');
    Utils.hideLoading('loginIcon');
    submitBtn.disabled = true;

    try {
        const formData = new FormData(event.target);
        const email = formData.get('email');
        const password = formData.get('password');
        const userType = formData.get('userType');

        let response;
        if (userType === 'customer') {
            response = await ApiService.auth.loginCustomer(email, password);
        } else {
            response = await ApiService.auth.loginMechanic(email, password);
        }

        // Store authentication data
        AuthState.isAuthenticated = true;
        AuthState.token = response.token;
        AuthState.userType = userType.toUpperCase();
        AuthState.currentUser = response[userType];

        // Save to localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('userType', userType.toUpperCase());
        localStorage.setItem('currentUser', JSON.stringify(response[userType]));

        // Show success message
        Utils.showAlert('Login successful! Redirecting...', 'success');
        
        // Redirect after short delay
        setTimeout(() => {
            redirectToDashboard();
        }, 1500);

    } catch (error) {
        console.error('Login failed:', error);
        Utils.showAlert(error.message || 'Login failed. Please check your credentials.', 'danger');
    } finally {
        // Hide loading state
        Utils.hideLoading('loginSpinner');
        Utils.showLoading('loginIcon');
        submitBtn.disabled = false;
    }
}

// Handle registration form submission
async function handleRegistration(event) {
    event.preventDefault();
    
    const submitBtn = document.getElementById('registerBtn');
    const spinner = document.getElementById('registerSpinner');
    const icon = document.getElementById('registerIcon');
    
    // Validate form
    if (!validateRegistrationForm()) {
        return;
    }

    // Show loading state
    Utils.showLoading('registerSpinner');
    Utils.hideLoading('registerIcon');
    submitBtn.disabled = true;

    try {
        const formData = new FormData(event.target);
        const userType = formData.get('userType');
        
        // Prepare user data
        const userData = {
            firstName: formData.get('firstName'),
            lastName: formData.get('lastName'),
            email: formData.get('email'),
            password: formData.get('password'),
            phone: formData.get('phone'),
            address: formData.get('address')
        };

        // Add mechanic-specific fields
        if (userType === 'mechanic') {
            userData.specialization = formData.get('specialization');
            userData.experienceYears = parseInt(formData.get('experienceYears')) || 0;
            userData.hourlyRate = parseFloat(formData.get('hourlyRate')) || 0;
        }

        let response;
        if (userType === 'customer') {
            response = await ApiService.auth.registerCustomer(userData);
        } else {
            response = await ApiService.auth.registerMechanic(userData);
        }

        // Store authentication data
        AuthState.isAuthenticated = true;
        AuthState.token = response.token;
        AuthState.userType = userType.toUpperCase();
        AuthState.currentUser = response[userType];

        // Save to localStorage
        localStorage.setItem('token', response.token);
        localStorage.setItem('userType', userType.toUpperCase());
        localStorage.setItem('currentUser', JSON.stringify(response[userType]));

        // Show success message
        Utils.showAlert('Registration successful! Welcome to Mechanic On Demand!', 'success');
        
        // Redirect after short delay
        setTimeout(() => {
            redirectToDashboard();
        }, 2000);

    } catch (error) {
        console.error('Registration failed:', error);
        Utils.showAlert(error.message || 'Registration failed. Please try again.', 'danger');
    } finally {
        // Hide loading state
        Utils.hideLoading('registerSpinner');
        Utils.showLoading('registerIcon');
        submitBtn.disabled = false;
    }
}

// Validate registration form
function validateRegistrationForm() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const termsAccepted = document.getElementById('termsAndConditions').checked;

    // Check password match
    if (password !== confirmPassword) {
        Utils.showAlert('Passwords do not match. Please try again.', 'danger');
        return false;
    }

    // Check password length
    if (password.length < 6) {
        Utils.showAlert('Password must be at least 6 characters long.', 'danger');
        return false;
    }

    // Check terms acceptance
    if (!termsAccepted) {
        Utils.showAlert('Please accept the Terms and Conditions to continue.', 'danger');
        return false;
    }

    return true;
}

// Handle user type change
function handleUserTypeChange(event) {
    const userType = event.target.value;
    const mechanicFields = document.getElementById('mechanicFields');
    
    if (userType === 'mechanic') {
        mechanicFields.classList.remove('d-none');
        // Make mechanic fields required
        const requiredFields = ['specialization', 'experienceYears', 'hourlyRate'];
        requiredFields.forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field) {
                field.required = true;
            }
        });
    } else {
        mechanicFields.classList.add('d-none');
        // Make mechanic fields optional
        const optionalFields = ['specialization', 'experienceYears', 'hourlyRate'];
        optionalFields.forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field) {
                field.required = false;
            }
        });
    }
}

// Toggle password visibility
function togglePasswordVisibility() {
    const passwordInput = document.getElementById('password');
    const toggleBtn = document.getElementById('togglePassword');
    const icon = toggleBtn.querySelector('i');
    
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        passwordInput.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}

// Validate password match
function validatePasswordMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    
    if (confirmPassword && password !== confirmPassword) {
        confirmPassword.setCustomValidity('Passwords do not match');
    } else {
        confirmPassword.setCustomValidity('');
    }
}

// Redirect to appropriate dashboard
function redirectToDashboard() {
    if (AuthState.userType === 'CUSTOMER') {
        window.location.href = 'customer-dashboard.html';
    } else if (AuthState.userType === 'MECHANIC') {
        window.location.href = 'mechanic-dashboard.html';
    } else {
        window.location.href = 'index.html';
    }
}

// Logout function
function logout() {
    // Clear authentication state
    AuthState.isAuthenticated = false;
    AuthState.currentUser = null;
    AuthState.userType = null;
    AuthState.token = null;

    // Clear localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('userType');
    localStorage.removeItem('currentUser');

    // Redirect to home page
    window.location.href = 'index.html';
}

// Check if user is authenticated
function isAuthenticated() {
    return AuthState.isAuthenticated;
}

// Get current user
function getCurrentUser() {
    return AuthState.currentUser;
}

// Get user type
function getUserType() {
    return AuthState.userType;
}

// Get authentication token
function getToken() {
    return AuthState.token;
}

// Export functions for use in other files
window.AuthState = AuthState;
window.logout = logout;
window.isAuthenticated = isAuthenticated;
window.getCurrentUser = getCurrentUser;
window.getUserType = getUserType;
window.getToken = getToken;
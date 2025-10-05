// Authentication JavaScript for Mechanic On Demand Application

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeAuth();
});

/**
 * Initialize authentication functionality
 */
function initializeAuth() {
    // Initialize login form
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLogin);
    }
    
    // Initialize registration form
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegister);
    }
    
    // Initialize user type selection
    initializeUserTypeSelection();
    
    // Initialize password toggle
    initializePasswordToggle();
    
    // Set minimum date for booking
    setMinimumDate();
}

/**
 * Handle login form submission
 */
async function handleLogin(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const loginData = {
        usernameOrEmail: formData.get('usernameOrEmail') || document.getElementById('usernameOrEmail').value,
        password: formData.get('password') || document.getElementById('password').value
    };
    
    // Validate form data
    if (!loginData.usernameOrEmail || !loginData.password) {
        showAlert('Please fill in all required fields.', 'danger');
        return;
    }
    
    try {
        showLoading(e.target);
        
        const response = await makeApiRequest('/auth/login', {
            method: 'POST',
            body: JSON.stringify(loginData)
        });
        
        // Store authentication data
        localStorage.setItem('authToken', response.token);
        localStorage.setItem('currentUser', JSON.stringify({
            id: response.id,
            username: response.username,
            email: response.email,
            firstName: response.firstName,
            lastName: response.lastName,
            userType: response.userType
        }));
        
        // Update global variables
        window.MechanicOnDemand.authToken = response.token;
        window.MechanicOnDemand.currentUser = {
            id: response.id,
            username: response.username,
            email: response.email,
            firstName: response.firstName,
            lastName: response.lastName,
            userType: response.userType
        };
        
        showAlert('Login successful! Redirecting...', 'success');
        
        // Redirect based on user type
        setTimeout(() => {
            if (response.userType === 'CUSTOMER') {
                window.location.href = 'dashboard.html';
            } else if (response.userType === 'MECHANIC') {
                window.location.href = 'dashboard.html';
            } else {
                window.location.href = 'index.html';
            }
        }, 1500);
        
    } catch (error) {
        showAlert(error.message || 'Login failed. Please try again.', 'danger');
    } finally {
        hideLoading();
    }
}

/**
 * Handle registration form submission
 */
async function handleRegister(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const registerData = {
        username: formData.get('username') || document.getElementById('username').value,
        email: formData.get('email') || document.getElementById('email').value,
        password: formData.get('password') || document.getElementById('password').value,
        firstName: formData.get('firstName') || document.getElementById('firstName').value,
        lastName: formData.get('lastName') || document.getElementById('lastName').value,
        phone: formData.get('phone') || document.getElementById('phone').value,
        userType: getSelectedUserType()
    };
    
    // Validate form data
    if (!validateRegistrationData(registerData)) {
        return;
    }
    
    try {
        showLoading(e.target);
        
        const response = await makeApiRequest('/auth/register', {
            method: 'POST',
            body: JSON.stringify(registerData)
        });
        
        showAlert('Registration successful! Please login to continue.', 'success');
        
        // Redirect to login page
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
        
    } catch (error) {
        showAlert(error.message || 'Registration failed. Please try again.', 'danger');
    } finally {
        hideLoading();
    }
}

/**
 * Validate registration data
 */
function validateRegistrationData(data) {
    // Check required fields
    if (!data.username || !data.email || !data.password || !data.firstName || !data.lastName) {
        showAlert('Please fill in all required fields.', 'danger');
        return false;
    }
    
    // Validate email
    if (!isValidEmail(data.email)) {
        showAlert('Please enter a valid email address.', 'danger');
        return false;
    }
    
    // Validate password
    if (data.password.length < 6) {
        showAlert('Password must be at least 6 characters long.', 'danger');
        return false;
    }
    
    // Check password confirmation
    const confirmPassword = document.getElementById('confirmPassword').value;
    if (data.password !== confirmPassword) {
        showAlert('Passwords do not match.', 'danger');
        return false;
    }
    
    // Validate phone if provided
    if (data.phone && !isValidPhone(data.phone)) {
        showAlert('Please enter a valid phone number.', 'danger');
        return false;
    }
    
    // Validate user type
    if (!data.userType) {
        showAlert('Please select your user type.', 'danger');
        return false;
    }
    
    return true;
}

/**
 * Initialize user type selection
 */
function initializeUserTypeSelection() {
    const userTypeButtons = document.querySelectorAll('.user-type-btn');
    
    userTypeButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Remove active class from all buttons
            userTypeButtons.forEach(btn => btn.classList.remove('active'));
            
            // Add active class to clicked button
            this.classList.add('active');
            
            // Show/hide mechanic fields
            const userType = this.dataset.type;
            const mechanicFields = document.getElementById('mechanicFields');
            
            if (userType === 'MECHANIC' && mechanicFields) {
                mechanicFields.classList.remove('d-none');
            } else if (mechanicFields) {
                mechanicFields.classList.add('d-none');
            }
        });
    });
}

/**
 * Get selected user type
 */
function getSelectedUserType() {
    const activeButton = document.querySelector('.user-type-btn.active');
    return activeButton ? activeButton.dataset.type : null;
}

/**
 * Initialize password toggle functionality
 */
function initializePasswordToggle() {
    const toggleButtons = document.querySelectorAll('#togglePassword');
    
    toggleButtons.forEach(button => {
        button.addEventListener('click', function() {
            const passwordInput = this.parentElement.querySelector('input');
            const icon = this.querySelector('i');
            
            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
            }
        });
    });
}

/**
 * Set minimum date for booking forms
 */
function setMinimumDate() {
    const dateInputs = document.querySelectorAll('input[type="date"]');
    const today = new Date().toISOString().split('T')[0];
    
    dateInputs.forEach(input => {
        input.min = today;
    });
}

/**
 * Auto-fill demo credentials
 */
function fillDemoCredentials(type) {
    const usernameField = document.getElementById('usernameOrEmail');
    const passwordField = document.getElementById('password');
    
    if (type === 'customer') {
        if (usernameField) usernameField.value = 'customer@demo.com';
        if (passwordField) passwordField.value = 'password123';
    } else if (type === 'mechanic') {
        if (usernameField) usernameField.value = 'mechanic@demo.com';
        if (passwordField) passwordField.value = 'password123';
    }
}

// Add demo credential buttons if they exist
document.addEventListener('DOMContentLoaded', function() {
    // Add demo buttons to login page
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        const demoButtons = document.createElement('div');
        demoButtons.className = 'mt-3 text-center';
        demoButtons.innerHTML = `
            <small class="text-muted">Try demo accounts:</small><br>
            <button type="button" class="btn btn-sm btn-outline-primary me-2" onclick="fillDemoCredentials('customer')">
                Customer Demo
            </button>
            <button type="button" class="btn btn-sm btn-outline-success" onclick="fillDemoCredentials('mechanic')">
                Mechanic Demo
            </button>
        `;
        loginForm.appendChild(demoButtons);
    }
});

// Make functions globally available
window.fillDemoCredentials = fillDemoCredentials;
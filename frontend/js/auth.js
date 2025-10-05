/**
 * Authentication JavaScript
 * Handles login and registration
 */

// API Base URL - Change this to your backend URL
const API_URL = 'http://localhost:8080/api';

// Login Form Handler
if (document.getElementById('loginForm')) {
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        
        showLoading(true);
        
        try {
            // Make POST request to login endpoint
            const response = await fetch(`${API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, password })
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Save user data and token to localStorage
                localStorage.setItem('token', data.data.token);
                localStorage.setItem('userId', data.data.id);
                localStorage.setItem('userName', data.data.name);
                localStorage.setItem('userEmail', data.data.email);
                localStorage.setItem('userRole', data.data.role);
                
                showAlert('Login successful! Redirecting...', 'success');
                setTimeout(() => {
                    window.location.href = 'dashboard.html';
                }, 1500);
            } else {
                showAlert(data.message, 'danger');
            }
        } catch (error) {
            showAlert('Error connecting to server', 'danger');
            console.error('Login error:', error);
        } finally {
            showLoading(false);
        }
    });
}

// Register Form Handler
if (document.getElementById('registerForm')) {
    document.getElementById('registerForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const role = document.querySelector('input[name="role"]:checked').value;
        const name = document.getElementById('name').value;
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;
        const phone = document.getElementById('phone').value;
        const address = document.getElementById('address').value;
        const latitude = document.getElementById('latitude').value;
        const longitude = document.getElementById('longitude').value;
        
        const requestData = {
            role,
            name,
            email,
            password,
            phone,
            address,
            latitude: latitude ? parseFloat(latitude) : null,
            longitude: longitude ? parseFloat(longitude) : null
        };
        
        // Add mechanic-specific fields
        if (role === 'MECHANIC') {
            requestData.specialty = document.getElementById('specialty').value;
            requestData.hourlyRate = parseFloat(document.getElementById('hourlyRate').value);
            requestData.monthlySubscription = 999.0;
            requestData.yearlySubscription = 9999.0;
        }
        
        showLoading(true);
        
        try {
            const response = await fetch(`${API_URL}/auth/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestData)
            });
            
            const data = await response.json();
            
            if (data.success) {
                // Save user data and token
                localStorage.setItem('token', data.data.token);
                localStorage.setItem('userId', data.data.id);
                localStorage.setItem('userName', data.data.name);
                localStorage.setItem('userEmail', data.data.email);
                localStorage.setItem('userRole', data.data.role);
                
                showAlert('Registration successful! Redirecting...', 'success');
                setTimeout(() => {
                    window.location.href = 'dashboard.html';
                }, 1500);
            } else {
                showAlert(data.message, 'danger');
            }
        } catch (error) {
            showAlert('Error connecting to server', 'danger');
            console.error('Registration error:', error);
        } finally {
            showLoading(false);
        }
    });
}

// Get User Location
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                document.getElementById('latitude').value = position.coords.latitude;
                document.getElementById('longitude').value = position.coords.longitude;
                showAlert('Location obtained successfully!', 'success');
            },
            (error) => {
                showAlert('Unable to get location. Please enable location services.', 'warning');
                console.error('Location error:', error);
            }
        );
    } else {
        showAlert('Geolocation is not supported by this browser.', 'warning');
    }
}

// Show Alert Message
function showAlert(message, type) {
    const alertDiv = document.getElementById('alertMessage');
    if (alertDiv) {
        alertDiv.className = `alert alert-${type}`;
        alertDiv.textContent = message;
        alertDiv.classList.remove('d-none');
        
        setTimeout(() => {
            alertDiv.classList.add('d-none');
        }, 5000);
    }
}

// Show/Hide Loading Spinner
function showLoading(show) {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.style.display = show ? 'block' : 'none';
    }
}

// Logout Function
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

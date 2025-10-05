/**
 * 🔧 Mechanic On Demand - Main Application JavaScript
 * 
 * This file contains the main application logic for the frontend.
 * It handles API calls, data management, and user interactions.
 */

// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// Global state management
const AppState = {
    currentUser: null,
    token: null,
    pricing: [],
    mechanics: []
};

// Utility Functions
const Utils = {
    // Show loading spinner
    showLoading: (elementId) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.classList.remove('d-none');
        }
    },

    // Hide loading spinner
    hideLoading: (elementId) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.classList.add('d-none');
        }
    },

    // Show alert message
    showAlert: (message, type = 'info', containerId = 'alertContainer') => {
        const container = document.getElementById(containerId);
        if (container) {
            const alertHtml = `
                <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                    <i class="fas fa-${type === 'success' ? 'check-circle' : type === 'error' ? 'exclamation-circle' : 'info-circle'} me-2"></i>
                    ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            `;
            container.innerHTML = alertHtml;
        }
    },

    // Clear alerts
    clearAlerts: (containerId = 'alertContainer') => {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = '';
        }
    },

    // Format currency
    formatCurrency: (amount) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    },

    // Format date
    formatDate: (dateString) => {
        return new Date(dateString).toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    },

    // Format time
    formatTime: (timeString) => {
        return new Date(`2000-01-01T${timeString}`).toLocaleTimeString('en-US', {
            hour: 'numeric',
            minute: '2-digit',
            hour12: true
        });
    },

    // Get current location
    getCurrentLocation: () => {
        return new Promise((resolve, reject) => {
            if (!navigator.geolocation) {
                reject(new Error('Geolocation is not supported by this browser.'));
                return;
            }

            navigator.geolocation.getCurrentPosition(
                (position) => {
                    resolve({
                        latitude: position.coords.latitude,
                        longitude: position.coords.longitude
                    });
                },
                (error) => {
                    reject(error);
                }
            );
        });
    }
};

// API Service
const ApiService = {
    // Make HTTP request
    request: async (endpoint, options = {}) => {
        const url = `${API_BASE_URL}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };

        // Add authorization header if token exists
        if (AppState.token) {
            config.headers.Authorization = `Bearer ${AppState.token}`;
        }

        try {
            const response = await fetch(url, config);
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.error || `HTTP error! status: ${response.status}`);
            }

            return data;
        } catch (error) {
            console.error('API request failed:', error);
            throw error;
        }
    },

    // Authentication endpoints
    auth: {
        loginCustomer: (email, password) => {
            return ApiService.request('/auth/customer/login', {
                method: 'POST',
                body: JSON.stringify({ email, password })
            });
        },

        loginMechanic: (email, password) => {
            return ApiService.request('/auth/mechanic/login', {
                method: 'POST',
                body: JSON.stringify({ email, password })
            });
        },

        registerCustomer: (customerData) => {
            return ApiService.request('/auth/customer/register', {
                method: 'POST',
                body: JSON.stringify(customerData)
            });
        },

        registerMechanic: (mechanicData) => {
            return ApiService.request('/auth/mechanic/register', {
                method: 'POST',
                body: JSON.stringify(mechanicData)
            });
        }
    },

    // Pricing endpoints
    pricing: {
        getAll: () => ApiService.request('/pricing'),
        getByServiceType: (serviceType) => ApiService.request(`/pricing/${serviceType}`)
    },

    // Mechanic endpoints
    mechanics: {
        getAll: () => ApiService.request('/mechanics'),
        getAvailable: () => ApiService.request('/mechanics/available'),
        getNearby: (lat, lng, radius = 10) => {
            return ApiService.request(`/mechanics/nearby?lat=${lat}&lng=${lng}&radius=${radius}`);
        },
        getBySpecialization: (specialization) => {
            return ApiService.request(`/mechanics/specialization?specialization=${specialization}`);
        }
    },

    // Booking endpoints
    bookings: {
        create: (bookingData) => {
            return ApiService.request('/bookings', {
                method: 'POST',
                body: JSON.stringify(bookingData)
            });
        },
        getAll: () => ApiService.request('/bookings'),
        getById: (id) => ApiService.request(`/bookings/${id}`),
        getByCustomer: (customerId) => ApiService.request(`/bookings/customer/${customerId}`),
        getByMechanic: (mechanicId) => ApiService.request(`/bookings/mechanic/${mechanicId}`),
        accept: (id, mechanicId) => {
            return ApiService.request(`/bookings/${id}/accept`, {
                method: 'PUT',
                body: JSON.stringify({ mechanicId })
            });
        },
        reject: (id, mechanicId) => {
            return ApiService.request(`/bookings/${id}/reject`, {
                method: 'PUT',
                body: JSON.stringify({ mechanicId })
            });
        },
        complete: (id, mechanicId, actualDuration) => {
            return ApiService.request(`/bookings/${id}/complete`, {
                method: 'PUT',
                body: JSON.stringify({ mechanicId, actualDuration })
            });
        },
        rate: (id, customerId, rating, feedback) => {
            return ApiService.request(`/bookings/${id}/rate`, {
                method: 'PUT',
                body: JSON.stringify({ customerId, rating, feedback })
            });
        },
        cancel: (id, userId, userType) => {
            return ApiService.request(`/bookings/${id}/cancel`, {
                method: 'PUT',
                body: JSON.stringify({ userId, userType })
            });
        }
    }
};

// Initialize application
document.addEventListener('DOMContentLoaded', () => {
    console.log('🔧 Mechanic On Demand App Initialized');
    
    // Load pricing data
    loadPricingData();
    
    // Set minimum date for booking form
    setMinDate();
    
    // Initialize event listeners
    initializeEventListeners();
});

// Load pricing data
async function loadPricingData() {
    try {
        const pricing = await ApiService.pricing.getAll();
        AppState.pricing = pricing;
        updatePricingDisplay();
    } catch (error) {
        console.error('Failed to load pricing data:', error);
    }
}

// Update pricing display on homepage
function updatePricingDisplay() {
    const container = document.getElementById('pricing-cards');
    if (!container || AppState.pricing.length === 0) return;

    const pricingHtml = AppState.pricing.map((pricing, index) => `
        <div class="col-lg-4 col-md-6">
            <div class="pricing-card ${index === 1 ? 'featured' : ''}">
                <h5 class="fw-bold">${pricing.serviceType}</h5>
                <div class="pricing-price">${Utils.formatCurrency(pricing.monthlyPrice)}</div>
                <div class="pricing-period">per month</div>
                <div class="pricing-price text-muted">${Utils.formatCurrency(pricing.yearlyPrice)}</div>
                <div class="pricing-period">per year</div>
                <p class="text-muted">${pricing.description}</p>
                <a href="booking.html" class="btn btn-primary">Choose Plan</a>
            </div>
        </div>
    `).join('');

    container.innerHTML = pricingHtml;
}

// Set minimum date for booking form
function setMinDate() {
    const dateInput = document.getElementById('preferredDate');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.min = today;
    }
}

// Initialize event listeners
function initializeEventListeners() {
    // Service type change handler for booking form
    const serviceTypeSelect = document.getElementById('serviceType');
    if (serviceTypeSelect) {
        serviceTypeSelect.addEventListener('change', handleServiceTypeChange);
    }

    // Get current location button
    const getLocationBtn = document.getElementById('getCurrentLocation');
    if (getLocationBtn) {
        getLocationBtn.addEventListener('click', handleGetCurrentLocation);
    }

    // Booking form submission
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSubmission);
    }
}

// Handle service type change
function handleServiceTypeChange(event) {
    const serviceType = event.target.value;
    updatePricingInfo(serviceType);
    loadNearbyMechanics(serviceType);
}

// Update pricing information
function updatePricingInfo(serviceType) {
    const pricingInfo = document.getElementById('pricingInfo');
    if (!pricingInfo) return;

    if (!serviceType) {
        pricingInfo.innerHTML = '<p class="text-muted">Select a service type to see pricing</p>';
        return;
    }

    const pricing = AppState.pricing.find(p => p.serviceType === serviceType);
    if (pricing) {
        pricingInfo.innerHTML = `
            <div class="text-center">
                <h6 class="fw-bold text-success">${pricing.serviceType}</h6>
                <div class="pricing-price text-success">${Utils.formatCurrency(pricing.monthlyPrice)}</div>
                <div class="pricing-period">Monthly Plan</div>
                <div class="pricing-price text-muted">${Utils.formatCurrency(pricing.yearlyPrice)}</div>
                <div class="pricing-period">Yearly Plan</div>
                <p class="text-muted small">${pricing.description}</p>
            </div>
        `;
    } else {
        pricingInfo.innerHTML = '<p class="text-muted">Pricing not available for this service</p>';
    }
}

// Load nearby mechanics
async function loadNearbyMechanics(serviceType) {
    const mechanicsList = document.getElementById('mechanicsList');
    if (!mechanicsList) return;

    mechanicsList.innerHTML = '<p class="text-muted">Loading nearby mechanics...</p>';

    try {
        // Get user's current location
        const location = await Utils.getCurrentLocation();
        const mechanics = await ApiService.mechanics.getNearby(location.latitude, location.longitude, 10);
        
        // Filter by specialization if service type is selected
        let filteredMechanics = mechanics;
        if (serviceType) {
            const specializationMap = {
                'Engine Repair': 'Engine Specialist',
                'Electrical System': 'Electrical Systems',
                'Brake Service': 'Brake Specialist',
                'Transmission': 'Transmission',
                'Emergency Service': 'Emergency Service'
            };
            
            const specialization = specializationMap[serviceType];
            if (specialization) {
                filteredMechanics = mechanics.filter(m => m.specialization === specialization);
            }
        }

        if (filteredMechanics.length === 0) {
            mechanicsList.innerHTML = '<p class="text-muted">No mechanics available for this service in your area</p>';
            return;
        }

        const mechanicsHtml = filteredMechanics.slice(0, 3).map(mechanic => `
            <div class="d-flex align-items-center mb-3 p-2 border rounded">
                <div class="flex-shrink-0">
                    <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style="width: 40px; height: 40px;">
                        <i class="fas fa-wrench"></i>
                    </div>
                </div>
                <div class="flex-grow-1 ms-3">
                    <h6 class="mb-1">${mechanic.firstName} ${mechanic.lastName}</h6>
                    <p class="text-muted mb-1 small">${mechanic.specialization}</p>
                    <div class="d-flex align-items-center">
                        <div class="text-warning me-2">
                            ${'★'.repeat(Math.floor(mechanic.rating))}${'☆'.repeat(5 - Math.floor(mechanic.rating))}
                        </div>
                        <span class="text-muted small">${mechanic.rating}/5 (${mechanic.totalRatings} reviews)</span>
                    </div>
                </div>
                <div class="text-end">
                    <div class="fw-bold text-success">${Utils.formatCurrency(mechanic.hourlyRate)}/hr</div>
                    <div class="text-muted small">Available</div>
                </div>
            </div>
        `).join('');

        mechanicsList.innerHTML = mechanicsHtml;
    } catch (error) {
        console.error('Failed to load mechanics:', error);
        mechanicsList.innerHTML = '<p class="text-muted">Unable to load mechanics. Please try again later.</p>';
    }
}

// Handle get current location
async function handleGetCurrentLocation() {
    const locationInput = document.getElementById('location');
    if (!locationInput) return;

    try {
        const location = await Utils.getCurrentLocation();
        // In a real app, you would reverse geocode the coordinates to get an address
        locationInput.value = `${location.latitude}, ${location.longitude}`;
        Utils.showAlert('Location detected successfully!', 'success');
    } catch (error) {
        console.error('Failed to get location:', error);
        Utils.showAlert('Unable to detect your location. Please enter it manually.', 'warning');
    }
}

// Handle booking form submission
async function handleBookingSubmission(event) {
    event.preventDefault();
    
    const submitBtn = document.getElementById('submitBooking');
    const spinner = document.getElementById('bookingSpinner');
    const icon = document.getElementById('bookingIcon');
    
    // Show loading state
    Utils.showLoading('bookingSpinner');
    Utils.hideLoading('bookingIcon');
    submitBtn.disabled = true;

    try {
        const formData = new FormData(event.target);
        const bookingData = {
            customerId: 1, // In a real app, this would come from authentication
            mechanicId: 1, // In a real app, this would be selected by the user
            serviceType: formData.get('serviceType'),
            description: formData.get('description'),
            vehicleInfo: `${formData.get('vehicleYear')} ${formData.get('vehicleMake')} ${formData.get('vehicleModel')} ${formData.get('vehicleColor')}`,
            bookingDate: `${formData.get('preferredDate')}T${formData.get('preferredTime')}:00`
        };

        const booking = await ApiService.bookings.create(bookingData);
        
        Utils.showAlert('Booking request submitted successfully! A mechanic will contact you soon.', 'success');
        event.target.reset();
        
    } catch (error) {
        console.error('Booking submission failed:', error);
        Utils.showAlert('Failed to submit booking request. Please try again.', 'danger');
    } finally {
        // Hide loading state
        Utils.hideLoading('bookingSpinner');
        Utils.showLoading('bookingIcon');
        submitBtn.disabled = false;
    }
}

// Export for use in other files
window.AppState = AppState;
window.Utils = Utils;
window.ApiService = ApiService;
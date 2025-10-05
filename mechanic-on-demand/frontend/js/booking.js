/**
 * 📅 Booking JavaScript
 * 
 * This file handles all booking-related functionality including
 * form validation, mechanic selection, and booking submission.
 */

// Initialize booking functionality when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    initializeBooking();
    setupBookingEventListeners();
});

// Initialize booking
function initializeBooking() {
    // Set minimum date to today
    setMinDate();
    
    // Load initial data
    loadPricingData();
    loadNearbyMechanics();
    
    // Initialize form validation
    initializeFormValidation();
}

// Setup event listeners
function setupBookingEventListeners() {
    // Service type change
    const serviceTypeSelect = document.getElementById('serviceType');
    if (serviceTypeSelect) {
        serviceTypeSelect.addEventListener('change', handleServiceTypeChange);
    }

    // Get current location
    const getLocationBtn = document.getElementById('getCurrentLocation');
    if (getLocationBtn) {
        getLocationBtn.addEventListener('click', handleGetCurrentLocation);
    }

    // Form submission
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSubmission);
    }

    // Real-time form validation
    setupRealTimeValidation();
}

// Set minimum date for booking
function setMinDate() {
    const dateInput = document.getElementById('preferredDate');
    if (dateInput) {
        const today = new Date();
        const tomorrow = new Date(today);
        tomorrow.setDate(tomorrow.getDate() + 1);
        dateInput.min = tomorrow.toISOString().split('T')[0];
    }
}

// Load pricing data
async function loadPricingData() {
    try {
        const pricing = await ApiService.pricing.getAll();
        AppState.pricing = pricing;
    } catch (error) {
        console.error('Failed to load pricing data:', error);
    }
}

// Load nearby mechanics
async function loadNearbyMechanics() {
    const mechanicsList = document.getElementById('mechanicsList');
    if (!mechanicsList) return;

    mechanicsList.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"></div><p class="mt-2 text-muted">Loading mechanics...</p></div>';

    try {
        // Get user's current location
        const location = await Utils.getCurrentLocation();
        const mechanics = await ApiService.mechanics.getNearby(location.latitude, location.longitude, 10);
        
        displayMechanics(mechanics);
    } catch (error) {
        console.error('Failed to load mechanics:', error);
        mechanicsList.innerHTML = '<p class="text-muted">Unable to load mechanics. Please try again later.</p>';
    }
}

// Display mechanics list
function displayMechanics(mechanics) {
    const mechanicsList = document.getElementById('mechanicsList');
    if (!mechanicsList) return;

    if (mechanics.length === 0) {
        mechanicsList.innerHTML = '<p class="text-muted">No mechanics available in your area</p>';
        return;
    }

    const mechanicsHtml = mechanics.slice(0, 5).map(mechanic => `
        <div class="mechanic-card border rounded p-3 mb-3" data-mechanic-id="${mechanic.id}">
            <div class="d-flex align-items-center">
                <div class="flex-shrink-0">
                    <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style="width: 50px; height: 50px;">
                        <i class="fas fa-wrench fa-lg"></i>
                    </div>
                </div>
                <div class="flex-grow-1 ms-3">
                    <h6 class="mb-1 fw-bold">${mechanic.firstName} ${mechanic.lastName}</h6>
                    <p class="text-muted mb-1 small">${mechanic.specialization || 'General Mechanic'}</p>
                    <div class="d-flex align-items-center mb-2">
                        <div class="text-warning me-2">
                            ${generateStarRating(mechanic.rating)}
                        </div>
                        <span class="text-muted small">${mechanic.rating}/5 (${mechanic.totalRatings} reviews)</span>
                    </div>
                    <div class="d-flex align-items-center">
                        <span class="badge bg-success me-2">Available</span>
                        <span class="text-muted small">${mechanic.experienceYears || 0} years experience</span>
                    </div>
                </div>
                <div class="text-end">
                    <div class="fw-bold text-success fs-5">${Utils.formatCurrency(mechanic.hourlyRate || 0)}/hr</div>
                    <button class="btn btn-sm btn-outline-primary mt-2" onclick="selectMechanic(${mechanic.id})">
                        Select
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    mechanicsList.innerHTML = mechanicsHtml;
}

// Generate star rating HTML
function generateStarRating(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 >= 0.5;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    
    let stars = '★'.repeat(fullStars);
    if (hasHalfStar) stars += '☆';
    stars += '☆'.repeat(emptyStars);
    
    return stars;
}

// Select mechanic
function selectMechanic(mechanicId) {
    // Remove previous selection
    document.querySelectorAll('.mechanic-card').forEach(card => {
        card.classList.remove('border-primary', 'bg-light');
    });
    
    // Highlight selected mechanic
    const selectedCard = document.querySelector(`[data-mechanic-id="${mechanicId}"]`);
    if (selectedCard) {
        selectedCard.classList.add('border-primary', 'bg-light');
    }
    
    // Store selected mechanic ID
    window.selectedMechanicId = mechanicId;
    
    Utils.showAlert('Mechanic selected! You can now submit your booking request.', 'success');
}

// Handle service type change
function handleServiceTypeChange(event) {
    const serviceType = event.target.value;
    updatePricingInfo(serviceType);
    filterMechanicsByService(serviceType);
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

// Filter mechanics by service type
async function filterMechanicsByService(serviceType) {
    const mechanicsList = document.getElementById('mechanicsList');
    if (!mechanicsList) return;

    mechanicsList.innerHTML = '<div class="text-center"><div class="spinner-border text-primary" role="status"></div><p class="mt-2 text-muted">Filtering mechanics...</p></div>';

    try {
        const mechanics = await ApiService.mechanics.getBySpecialization(serviceType);
        displayMechanics(mechanics);
    } catch (error) {
        console.error('Failed to filter mechanics:', error);
        // Fallback to loading all nearby mechanics
        loadNearbyMechanics();
    }
}

// Handle get current location
async function handleGetCurrentLocation() {
    const locationInput = document.getElementById('location');
    if (!locationInput) return;

    const getLocationBtn = document.getElementById('getCurrentLocation');
    const originalText = getLocationBtn.innerHTML;
    
    getLocationBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Detecting...';
    getLocationBtn.disabled = true;

    try {
        const location = await Utils.getCurrentLocation();
        // In a real app, you would reverse geocode the coordinates to get an address
        locationInput.value = `${location.latitude}, ${location.longitude}`;
        Utils.showAlert('Location detected successfully!', 'success');
    } catch (error) {
        console.error('Failed to get location:', error);
        Utils.showAlert('Unable to detect your location. Please enter it manually.', 'warning');
    } finally {
        getLocationBtn.innerHTML = originalText;
        getLocationBtn.disabled = false;
    }
}

// Setup real-time form validation
function setupRealTimeValidation() {
    // Email validation
    const emailInput = document.getElementById('contactEmail');
    if (emailInput) {
        emailInput.addEventListener('blur', validateEmail);
    }

    // Phone validation
    const phoneInput = document.getElementById('contactPhone');
    if (phoneInput) {
        phoneInput.addEventListener('blur', validatePhone);
    }

    // Date validation
    const dateInput = document.getElementById('preferredDate');
    if (dateInput) {
        dateInput.addEventListener('change', validateDate);
    }
}

// Validate email
function validateEmail(event) {
    const email = event.target.value;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (email && !emailRegex.test(email)) {
        event.target.setCustomValidity('Please enter a valid email address');
        event.target.classList.add('is-invalid');
    } else {
        event.target.setCustomValidity('');
        event.target.classList.remove('is-invalid');
    }
}

// Validate phone
function validatePhone(event) {
    const phone = event.target.value;
    const phoneRegex = /^[\+]?[1-9][\d]{0,15}$/;
    
    if (phone && !phoneRegex.test(phone.replace(/\s/g, ''))) {
        event.target.setCustomValidity('Please enter a valid phone number');
        event.target.classList.add('is-invalid');
    } else {
        event.target.setCustomValidity('');
        event.target.classList.remove('is-invalid');
    }
}

// Validate date
function validateDate(event) {
    const selectedDate = new Date(event.target.value);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate <= today) {
        event.target.setCustomValidity('Please select a future date');
        event.target.classList.add('is-invalid');
    } else {
        event.target.setCustomValidity('');
        event.target.classList.remove('is-invalid');
    }
}

// Initialize form validation
function initializeFormValidation() {
    // Add custom validation messages
    const inputs = document.querySelectorAll('input[required], select[required], textarea[required]');
    inputs.forEach(input => {
        input.addEventListener('invalid', function(event) {
            event.target.classList.add('is-invalid');
        });
        
        input.addEventListener('input', function(event) {
            if (event.target.checkValidity()) {
                event.target.classList.remove('is-invalid');
                event.target.classList.add('is-valid');
            }
        });
    });
}

// Handle booking form submission
async function handleBookingSubmission(event) {
    event.preventDefault();
    
    const submitBtn = document.getElementById('submitBooking');
    const spinner = document.getElementById('bookingSpinner');
    const icon = document.getElementById('bookingIcon');
    
    // Validate form
    if (!event.target.checkValidity()) {
        event.target.classList.add('was-validated');
        Utils.showAlert('Please fill in all required fields correctly.', 'warning');
        return;
    }

    // Check if mechanic is selected
    if (!window.selectedMechanicId) {
        Utils.showAlert('Please select a mechanic before submitting your booking.', 'warning');
        return;
    }

    // Show loading state
    Utils.showLoading('bookingSpinner');
    Utils.hideLoading('bookingIcon');
    submitBtn.disabled = true;

    try {
        const formData = new FormData(event.target);
        
        // Prepare booking data
        const bookingData = {
            customerId: 1, // In a real app, this would come from authentication
            mechanicId: window.selectedMechanicId,
            serviceType: formData.get('serviceType'),
            description: formData.get('description'),
            vehicleInfo: `${formData.get('vehicleYear')} ${formData.get('vehicleMake')} ${formData.get('vehicleModel')} ${formData.get('vehicleColor')}`,
            bookingDate: `${formData.get('preferredDate')}T${formData.get('preferredTime')}:00`
        };

        const booking = await ApiService.bookings.create(bookingData);
        
        // Show success message
        Utils.showAlert(`
            <h5 class="alert-heading">Booking Request Submitted!</h5>
            <p>Your booking request has been submitted successfully. Here are the details:</p>
            <ul class="mb-0">
                <li><strong>Service:</strong> ${booking.serviceType}</li>
                <li><strong>Date:</strong> ${Utils.formatDate(booking.bookingDate)}</li>
                <li><strong>Status:</strong> <span class="badge status-pending">Pending</span></li>
            </ul>
            <p class="mt-2 mb-0">The mechanic will contact you soon to confirm the appointment.</p>
        `, 'success');
        
        // Reset form
        event.target.reset();
        event.target.classList.remove('was-validated');
        
        // Clear mechanic selection
        window.selectedMechanicId = null;
        document.querySelectorAll('.mechanic-card').forEach(card => {
            card.classList.remove('border-primary', 'bg-light');
        });
        
    } catch (error) {
        console.error('Booking submission failed:', error);
        Utils.showAlert(`Failed to submit booking request: ${error.message}`, 'danger');
    } finally {
        // Hide loading state
        Utils.hideLoading('bookingSpinner');
        Utils.showLoading('bookingIcon');
        submitBtn.disabled = false;
    }
}

// Export functions for global use
window.selectMechanic = selectMechanic;
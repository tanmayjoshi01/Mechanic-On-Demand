// Booking JavaScript for Mechanic On Demand Application

// Global variables
let selectedMechanic = null;
let availableMechanics = [];

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeBooking();
});

/**
 * Initialize booking functionality
 */
function initializeBooking() {
    // Check authentication
    if (!window.MechanicOnDemand.currentUser) {
        window.location.href = 'login.html';
        return;
    }
    
    // Initialize booking form
    const bookingForm = document.getElementById('bookingForm');
    if (bookingForm) {
        bookingForm.addEventListener('submit', handleBookingSearch);
    }
    
    // Initialize location button
    const locationBtn = document.getElementById('getLocationBtn');
    if (locationBtn) {
        locationBtn.addEventListener('click', getCurrentLocation);
    }
    
    // Initialize confirm booking button
    const confirmBtn = document.getElementById('confirmBookingBtn');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', confirmBooking);
    }
    
    // Load service categories
    loadServiceCategories();
    
    // Set default date and time
    setDefaultDateTime();
}

/**
 * Handle booking search form submission
 */
async function handleBookingSearch(e) {
    e.preventDefault();
    
    const formData = new FormData(e.target);
    const searchData = {
        problemDescription: formData.get('problemDescription') || document.getElementById('problemDescription').value,
        serviceCategory: formData.get('serviceCategory') || document.getElementById('serviceCategory').value,
        address: formData.get('address') || document.getElementById('address').value,
        bookingDate: formData.get('bookingDate') || document.getElementById('bookingDate').value,
        bookingTime: formData.get('bookingTime') || document.getElementById('bookingTime').value,
        specialInstructions: formData.get('specialInstructions') || document.getElementById('specialInstructions').value
    };
    
    // Validate form data
    if (!validateBookingData(searchData)) {
        return;
    }
    
    try {
        showLoading(e.target);
        
        // Get current location for nearby search
        const location = await getCurrentLocation();
        
        // Search for nearby mechanics
        const mechanics = await searchNearbyMechanics(location.latitude, location.longitude);
        
        // Display mechanics
        displayMechanics(mechanics);
        
        // Store search data for booking
        window.bookingData = searchData;
        window.bookingLocation = location;
        
    } catch (error) {
        showAlert(error.message || 'Failed to search for mechanics. Please try again.', 'danger');
    } finally {
        hideLoading();
    }
}

/**
 * Validate booking data
 */
function validateBookingData(data) {
    if (!data.problemDescription || !data.serviceCategory || !data.address || !data.bookingDate || !data.bookingTime) {
        showAlert('Please fill in all required fields.', 'danger');
        return false;
    }
    
    // Validate date is not in the past
    const selectedDate = new Date(data.bookingDate);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    
    if (selectedDate < today) {
        showAlert('Please select a future date.', 'danger');
        return false;
    }
    
    return true;
}

/**
 * Search for nearby mechanics
 */
async function searchNearbyMechanics(latitude, longitude) {
    try {
        const response = await makeApiRequest(`/users/mechanics/nearby?latitude=${latitude}&longitude=${longitude}&radius=10`);
        return response;
    } catch (error) {
        console.error('Error searching for mechanics:', error);
        throw new Error('Failed to find nearby mechanics');
    }
}

/**
 * Display available mechanics
 */
function displayMechanics(mechanics) {
    const mechanicsList = document.getElementById('mechanicsList');
    const mechanicCards = document.getElementById('mechanicCards');
    const mechanicCardsContainer = document.getElementById('mechanicCardsContainer');
    
    if (mechanics.length === 0) {
        mechanicsList.innerHTML = `
            <div class="text-center text-muted py-4">
                <i class="fas fa-search fa-2x mb-3"></i>
                <p>No mechanics found in your area. Try expanding your search radius.</p>
            </div>
        `;
        return;
    }
    
    // Store mechanics globally
    availableMechanics = mechanics;
    
    // Update mechanics list
    mechanicsList.innerHTML = `
        <div class="text-center text-success py-2">
            <i class="fas fa-check-circle me-2"></i>
            Found ${mechanics.length} mechanic(s) nearby
        </div>
    `;
    
    // Show mechanic cards
    mechanicCards.style.display = 'block';
    mechanicCardsContainer.innerHTML = '';
    
    mechanics.forEach((mechanic, index) => {
        const mechanicCard = createMechanicCard(mechanic, index);
        mechanicCardsContainer.appendChild(mechanicCard);
    });
}

/**
 * Create mechanic card element
 */
function createMechanicCard(mechanic, index) {
    const card = document.createElement('div');
    card.className = 'col-md-6 col-lg-4 mb-3';
    card.innerHTML = `
        <div class="card mechanic-card h-100" data-mechanic-id="${mechanic.id}">
            <div class="card-body">
                <div class="d-flex align-items-center mb-3">
                    <div class="avatar me-3">
                        <i class="fas fa-user-circle fa-3x text-primary"></i>
                    </div>
                    <div>
                        <h6 class="mb-1">${mechanic.firstName} ${mechanic.lastName}</h6>
                        <small class="text-muted">${mechanic.specialization || 'General Mechanic'}</small>
                    </div>
                </div>
                
                <div class="mb-3">
                    <div class="d-flex justify-content-between align-items-center mb-1">
                        <small class="text-muted">Rating</small>
                        <span class="rating-stars">
                            ${generateStarRating(mechanic.rating || 0)}
                        </span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center mb-1">
                        <small class="text-muted">Experience</small>
                        <small class="fw-bold">${mechanic.experienceYears || 0} years</small>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                        <small class="text-muted">Hourly Rate</small>
                        <small class="fw-bold text-success">${formatCurrency(mechanic.hourlyRate || 0)}</small>
                    </div>
                </div>
                
                <div class="d-grid">
                    <button class="btn btn-primary btn-sm select-mechanic-btn" data-mechanic-index="${index}">
                        <i class="fas fa-check me-1"></i>Select Mechanic
                    </button>
                </div>
            </div>
        </div>
    `;
    
    // Add click event listener
    const selectBtn = card.querySelector('.select-mechanic-btn');
    selectBtn.addEventListener('click', () => selectMechanic(index));
    
    return card;
}

/**
 * Generate star rating HTML
 */
function generateStarRating(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
    
    let stars = '';
    
    // Full stars
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fas fa-star"></i>';
    }
    
    // Half star
    if (hasHalfStar) {
        stars += '<i class="fas fa-star-half-alt"></i>';
    }
    
    // Empty stars
    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="fas fa-star text-muted"></i>';
    }
    
    return stars;
}

/**
 * Select a mechanic
 */
function selectMechanic(index) {
    selectedMechanic = availableMechanics[index];
    
    // Update UI
    document.querySelectorAll('.mechanic-card').forEach(card => {
        card.classList.remove('selected');
    });
    
    const selectedCard = document.querySelector(`[data-mechanic-index="${index}"]`).closest('.mechanic-card');
    selectedCard.classList.add('selected');
    
    // Show confirmation modal
    showMechanicConfirmationModal();
}

/**
 * Show mechanic confirmation modal
 */
function showMechanicConfirmationModal() {
    const modal = new bootstrap.Modal(document.getElementById('mechanicModal'));
    const modalBody = document.getElementById('selectedMechanicInfo');
    
    modalBody.innerHTML = `
        <div class="row">
            <div class="col-md-4">
                <i class="fas fa-user-circle fa-5x text-primary"></i>
            </div>
            <div class="col-md-8">
                <h5>${selectedMechanic.firstName} ${selectedMechanic.lastName}</h5>
                <p class="text-muted">${selectedMechanic.specialization || 'General Mechanic'}</p>
                
                <div class="row mb-3">
                    <div class="col-6">
                        <small class="text-muted">Rating</small>
                        <div class="rating-stars">${generateStarRating(selectedMechanic.rating || 0)}</div>
                    </div>
                    <div class="col-6">
                        <small class="text-muted">Experience</small>
                        <div class="fw-bold">${selectedMechanic.experienceYears || 0} years</div>
                    </div>
                </div>
                
                <div class="row mb-3">
                    <div class="col-6">
                        <small class="text-muted">Hourly Rate</small>
                        <div class="fw-bold text-success">${formatCurrency(selectedMechanic.hourlyRate || 0)}</div>
                    </div>
                    <div class="col-6">
                        <small class="text-muted">Response Time</small>
                        <div class="fw-bold">Within 30 mins</div>
                    </div>
                </div>
                
                <div class="alert alert-info">
                    <i class="fas fa-info-circle me-2"></i>
                    <strong>Service Details:</strong><br>
                    <small>Date: ${formatDate(window.bookingData.bookingDate)}</small><br>
                    <small>Time: ${formatTime(window.bookingData.bookingTime)}</small><br>
                    <small>Location: ${window.bookingData.address}</small>
                </div>
            </div>
        </div>
    `;
    
    modal.show();
}

/**
 * Confirm booking
 */
async function confirmBooking() {
    if (!selectedMechanic || !window.bookingData) {
        showAlert('Please select a mechanic first.', 'danger');
        return;
    }
    
    try {
        showLoading(document.getElementById('confirmBookingBtn'));
        
        // Create booking request
        const bookingRequest = {
            mechanicId: selectedMechanic.id,
            serviceId: 1, // Default service ID - in real app, this would be selected
            bookingDate: window.bookingData.bookingDate,
            bookingTime: window.bookingData.bookingTime,
            address: window.bookingData.address,
            latitude: window.bookingLocation.latitude,
            longitude: window.bookingLocation.longitude,
            problemDescription: window.bookingData.problemDescription,
            specialInstructions: window.bookingData.specialInstructions
        };
        
        // Make booking request
        const response = await makeApiRequest('/bookings', {
            method: 'POST',
            body: JSON.stringify(bookingRequest)
        });
        
        showAlert('Booking confirmed successfully! You will receive a notification when the mechanic accepts your request.', 'success');
        
        // Close modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('mechanicModal'));
        modal.hide();
        
        // Redirect to dashboard
        setTimeout(() => {
            window.location.href = 'dashboard.html';
        }, 2000);
        
    } catch (error) {
        showAlert(error.message || 'Failed to confirm booking. Please try again.', 'danger');
    } finally {
        hideLoading();
    }
}

/**
 * Load service categories
 */
async function loadServiceCategories() {
    try {
        const response = await makeApiRequest('/services/categories');
        const categorySelect = document.getElementById('serviceCategory');
        
        if (categorySelect) {
            categorySelect.innerHTML = '<option value="">Select a service category</option>';
            
            response.forEach(category => {
                const option = document.createElement('option');
                option.value = category.id;
                option.textContent = category.name;
                categorySelect.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error loading service categories:', error);
    }
}

/**
 * Set default date and time
 */
function setDefaultDateTime() {
    const dateInput = document.getElementById('bookingDate');
    const timeInput = document.getElementById('bookingTime');
    
    if (dateInput) {
        const tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        dateInput.value = tomorrow.toISOString().split('T')[0];
    }
    
    if (timeInput) {
        timeInput.value = '09:00';
    }
}

/**
 * Get current location
 */
async function getCurrentLocation() {
    try {
        const location = await window.MechanicOnDemand.getCurrentLocation();
        
        // Update address field
        const addressField = document.getElementById('address');
        if (addressField && !addressField.value) {
            addressField.value = 'Current Location';
        }
        
        return location;
    } catch (error) {
        showAlert('Unable to get your current location. Please enter your address manually.', 'warning');
        throw error;
    }
}
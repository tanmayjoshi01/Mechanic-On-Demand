/**
 * Find Mechanics JavaScript
 * Handles searching and booking mechanics
 */

const API_URL = 'http://localhost:8080/api';

const token = localStorage.getItem('token');
const userId = localStorage.getItem('userId');
const userRole = localStorage.getItem('userRole');

if (!token || userRole !== 'CUSTOMER') {
    alert('Please login as a customer to book mechanics');
    window.location.href = 'login.html';
}

let selectedMechanic = null;

/**
 * Get User Location
 */
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                document.getElementById('latitude').value = position.coords.latitude;
                document.getElementById('longitude').value = position.coords.longitude;
                alert('Location obtained successfully!');
            },
            (error) => {
                alert('Unable to get location. Please enable location services.');
                console.error('Location error:', error);
            }
        );
    } else {
        alert('Geolocation is not supported by this browser.');
    }
}

/**
 * Search for Mechanics
 */
async function searchMechanics() {
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;
    const radius = document.getElementById('searchRadius').value || 10;
    const specialty = document.getElementById('filterSpecialty').value;
    
    if (!latitude || !longitude) {
        alert('Please get your location first');
        return;
    }
    
    try {
        const response = await fetch(
            `${API_URL}/mechanics/nearby?latitude=${latitude}&longitude=${longitude}&radius=${radius}`,
            {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }
        );
        
        const data = await response.json();
        
        if (data.success && data.data) {
            let mechanics = data.data;
            
            // Filter by specialty if selected
            if (specialty) {
                mechanics = mechanics.filter(m => 
                    m.specialty === specialty || m.specialty === 'All'
                );
            }
            
            displayMechanics(mechanics);
        } else {
            displayNoResults();
        }
    } catch (error) {
        console.error('Error searching mechanics:', error);
        alert('Error searching mechanics');
    }
}

/**
 * Display Mechanics
 */
function displayMechanics(mechanics) {
    const mechanicsList = document.getElementById('mechanicsList');
    mechanicsList.innerHTML = '';
    
    if (mechanics.length === 0) {
        displayNoResults();
        return;
    }
    
    mechanics.forEach(mechanic => {
        const mechanicCard = `
            <div class="col-md-4 mb-4">
                <div class="card mechanic-card h-100" onclick="selectMechanic(${mechanic.id}, '${mechanic.name}', ${mechanic.hourlyRate}, ${mechanic.monthlySubscription}, ${mechanic.yearlySubscription})">
                    <div class="card-body text-center">
                        <div class="mechanic-avatar">
                            <i class="fas fa-user"></i>
                        </div>
                        <h5 class="card-title">${mechanic.name}</h5>
                        <p class="card-text">
                            <span class="badge bg-primary">${mechanic.specialty}</span>
                            ${mechanic.available ? '<span class="badge bg-success">Available</span>' : '<span class="badge bg-secondary">Busy</span>'}
                        </p>
                        <div class="rating mb-2">
                            ${getRatingStars(mechanic.rating)}
                            <span>${(mechanic.rating || 0).toFixed(1)}</span>
                        </div>
                        <p class="text-muted mb-2">
                            <small><i class="fas fa-briefcase"></i> ${mechanic.totalJobs} jobs completed</small>
                        </p>
                        <hr>
                        <p class="mb-1"><strong>Hourly:</strong> $${mechanic.hourlyRate}</p>
                        <p class="mb-1"><strong>Monthly:</strong> $${mechanic.monthlySubscription}</p>
                        <p class="mb-3"><strong>Yearly:</strong> $${mechanic.yearlySubscription}</p>
                        <button class="btn btn-primary btn-sm w-100" ${!mechanic.available ? 'disabled' : ''}>
                            <i class="fas fa-calendar-check"></i> Book Now
                        </button>
                    </div>
                </div>
            </div>
        `;
        mechanicsList.innerHTML += mechanicCard;
    });
}

/**
 * Display No Results
 */
function displayNoResults() {
    document.getElementById('mechanicsList').innerHTML = `
        <div class="col-md-12 text-center">
            <p class="text-muted">No mechanics found in your area. Try increasing the search radius.</p>
        </div>
    `;
}

/**
 * Get Rating Stars HTML
 */
function getRatingStars(rating) {
    const fullStars = Math.floor(rating || 0);
    let stars = '';
    for (let i = 0; i < 5; i++) {
        if (i < fullStars) {
            stars += '<i class="fas fa-star"></i>';
        } else {
            stars += '<i class="far fa-star"></i>';
        }
    }
    return stars;
}

/**
 * Select Mechanic for Booking
 */
function selectMechanic(mechanicId, mechanicName, hourlyRate, monthlyRate, yearlyRate) {
    selectedMechanic = { id: mechanicId, name: mechanicName };
    
    // Update modal with mechanic info
    document.getElementById('mechanicId').value = mechanicId;
    document.getElementById('customerId').value = userId;
    document.getElementById('hourlyRate').textContent = hourlyRate;
    document.getElementById('monthlyRate').textContent = monthlyRate;
    document.getElementById('yearlyRate').textContent = yearlyRate;
    
    // Update subscription options text
    const subscriptionSelect = document.getElementById('subscriptionType');
    subscriptionSelect.innerHTML = `
        <option value="HOURLY">Hourly - $${hourlyRate}/hr</option>
        <option value="MONTHLY">Monthly - $${monthlyRate}</option>
        <option value="YEARLY">Yearly - $${yearlyRate}</option>
    `;
    
    // Show modal
    const modal = new bootstrap.Modal(document.getElementById('bookingModal'));
    modal.show();
}

/**
 * Handle Booking Form Submission
 */
document.getElementById('bookingForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const bookingData = {
        customerId: parseInt(userId),
        mechanicId: parseInt(document.getElementById('mechanicId').value),
        vehicleType: document.getElementById('vehicleType').value,
        vehicleModel: document.getElementById('vehicleModel').value,
        problemDescription: document.getElementById('problemDescription').value,
        subscriptionType: document.getElementById('subscriptionType').value,
        latitude: parseFloat(document.getElementById('latitude').value),
        longitude: parseFloat(document.getElementById('longitude').value),
        scheduledTime: new Date().toISOString()
    };
    
    try {
        const response = await fetch(`${API_URL}/bookings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(bookingData)
        });
        
        const data = await response.json();
        
        if (data.success) {
            alert('Booking created successfully!');
            bootstrap.Modal.getInstance(document.getElementById('bookingModal')).hide();
            document.getElementById('bookingForm').reset();
            window.location.href = 'dashboard.html';
        } else {
            alert('Failed to create booking: ' + data.message);
        }
    } catch (error) {
        console.error('Error creating booking:', error);
        alert('Error creating booking');
    }
});

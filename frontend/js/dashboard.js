/**
 * Dashboard JavaScript
 * Handles dashboard functionality for customers and mechanics
 */

const API_URL = 'http://localhost:8080/api';

// Check if user is logged in
const token = localStorage.getItem('token');
const userId = localStorage.getItem('userId');
const userName = localStorage.getItem('userName');
const userRole = localStorage.getItem('userRole');

if (!token) {
    window.location.href = 'login.html';
}

// Display user name
document.getElementById('userName').textContent = `Welcome, ${userName}!`;

// Show appropriate view based on role
if (userRole === 'CUSTOMER') {
    document.getElementById('customerView').style.display = 'block';
    loadCustomerDashboard();
} else if (userRole === 'MECHANIC') {
    document.getElementById('mechanicView').style.display = 'block';
    loadMechanicDashboard();
}

// Load bookings
loadBookings();

/**
 * Load Customer Dashboard
 */
async function loadCustomerDashboard() {
    try {
        const response = await fetch(`${API_URL}/bookings/customer/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (data.success && data.data) {
            const bookings = data.data;
            document.getElementById('totalBookings').textContent = bookings.length;
            document.getElementById('pendingBookings').textContent = 
                bookings.filter(b => b.status === 'PENDING').length;
            document.getElementById('completedBookings').textContent = 
                bookings.filter(b => b.status === 'COMPLETED').length;
        }
    } catch (error) {
        console.error('Error loading customer dashboard:', error);
    }
}

/**
 * Load Mechanic Dashboard
 */
async function loadMechanicDashboard() {
    try {
        // Get mechanic details
        const mechanicResponse = await fetch(`${API_URL}/mechanics/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const mechanicData = await mechanicResponse.json();
        
        if (mechanicData.success && mechanicData.data) {
            const mechanic = mechanicData.data;
            document.getElementById('totalJobs').textContent = mechanic.totalJobs || 0;
            document.getElementById('rating').textContent = (mechanic.rating || 0).toFixed(1);
            document.getElementById('availabilityToggle').checked = mechanic.available;
        }
        
        // Get bookings
        const bookingsResponse = await fetch(`${API_URL}/bookings/mechanic/${userId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const bookingsData = await bookingsResponse.json();
        
        if (bookingsData.success && bookingsData.data) {
            const bookings = bookingsData.data;
            document.getElementById('pendingRequests').textContent = 
                bookings.filter(b => b.status === 'PENDING').length;
        }
        
        // Add availability toggle handler
        document.getElementById('availabilityToggle').addEventListener('change', updateAvailability);
    } catch (error) {
        console.error('Error loading mechanic dashboard:', error);
    }
}

/**
 * Update Mechanic Availability
 */
async function updateAvailability(e) {
    const available = e.target.checked;
    
    try {
        const response = await fetch(`${API_URL}/mechanics/${userId}/availability?available=${available}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (data.success) {
            alert('Availability updated successfully!');
        } else {
            alert('Failed to update availability');
            e.target.checked = !available;
        }
    } catch (error) {
        console.error('Error updating availability:', error);
        alert('Error updating availability');
        e.target.checked = !available;
    }
}

/**
 * Load Bookings
 */
async function loadBookings() {
    const endpoint = userRole === 'CUSTOMER' 
        ? `/bookings/customer/${userId}` 
        : `/bookings/mechanic/${userId}`;
    
    try {
        const response = await fetch(`${API_URL}${endpoint}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (data.success && data.data && data.data.length > 0) {
            displayBookings(data.data);
        } else {
            document.getElementById('bookingsList').innerHTML = 
                '<p class="text-center text-muted">No bookings found</p>';
        }
    } catch (error) {
        console.error('Error loading bookings:', error);
        document.getElementById('bookingsList').innerHTML = 
            '<p class="text-center text-danger">Error loading bookings</p>';
    }
}

/**
 * Display Bookings
 */
function displayBookings(bookings) {
    const bookingsList = document.getElementById('bookingsList');
    bookingsList.innerHTML = '';
    
    bookings.forEach(booking => {
        const bookingCard = `
            <div class="card mb-3">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <h5>
                                ${booking.vehicleType} - ${booking.vehicleModel}
                                <span class="status-badge status-${booking.status.toLowerCase()}">${booking.status}</span>
                            </h5>
                            <p class="mb-1"><strong>Problem:</strong> ${booking.problemDescription}</p>
                            <p class="mb-1"><strong>Price:</strong> $${booking.price} (${booking.subscriptionType})</p>
                            <p class="mb-0 text-muted">
                                <small>Booked: ${new Date(booking.createdAt).toLocaleString()}</small>
                            </p>
                        </div>
                        <div class="col-md-4 text-end">
                            ${getBookingActions(booking)}
                        </div>
                    </div>
                </div>
            </div>
        `;
        bookingsList.innerHTML += bookingCard;
    });
}

/**
 * Get Booking Actions based on role and status
 */
function getBookingActions(booking) {
    if (userRole === 'MECHANIC') {
        if (booking.status === 'PENDING') {
            return `
                <button class="btn btn-success btn-sm mb-2" onclick="acceptBooking(${booking.id})">
                    <i class="fas fa-check"></i> Accept
                </button>
                <button class="btn btn-danger btn-sm" onclick="rejectBooking(${booking.id})">
                    <i class="fas fa-times"></i> Reject
                </button>
            `;
        } else if (booking.status === 'ACCEPTED') {
            return `
                <button class="btn btn-primary btn-sm" onclick="startBooking(${booking.id})">
                    <i class="fas fa-play"></i> Start
                </button>
            `;
        } else if (booking.status === 'IN_PROGRESS') {
            return `
                <button class="btn btn-success btn-sm" onclick="completeBooking(${booking.id})">
                    <i class="fas fa-check-circle"></i> Complete
                </button>
            `;
        }
    } else {
        // Customer actions
        if (booking.status === 'PENDING' || booking.status === 'ACCEPTED') {
            return `
                <button class="btn btn-danger btn-sm" onclick="cancelBooking(${booking.id})">
                    <i class="fas fa-times"></i> Cancel
                </button>
            `;
        }
    }
    return '';
}

/**
 * Booking Action Functions
 */
async function acceptBooking(bookingId) {
    await updateBookingStatus(bookingId, 'accept', 'Booking accepted successfully!');
}

async function rejectBooking(bookingId) {
    await updateBookingStatus(bookingId, 'reject', 'Booking rejected');
}

async function startBooking(bookingId) {
    await updateBookingStatus(bookingId, 'start', 'Booking started!');
}

async function completeBooking(bookingId) {
    await updateBookingStatus(bookingId, 'complete', 'Booking completed successfully!');
}

async function cancelBooking(bookingId) {
    if (confirm('Are you sure you want to cancel this booking?')) {
        await updateBookingStatus(bookingId, 'cancel', 'Booking cancelled');
    }
}

/**
 * Update Booking Status
 */
async function updateBookingStatus(bookingId, action, successMessage) {
    try {
        const response = await fetch(`${API_URL}/bookings/${bookingId}/${action}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        const data = await response.json();
        
        if (data.success) {
            alert(successMessage);
            loadBookings(); // Reload bookings
            if (userRole === 'MECHANIC') {
                loadMechanicDashboard(); // Refresh dashboard stats
            } else {
                loadCustomerDashboard();
            }
        } else {
            alert('Failed to update booking: ' + data.message);
        }
    } catch (error) {
        console.error('Error updating booking:', error);
        alert('Error updating booking');
    }
}

/**
 * Logout Function
 */
function logout() {
    localStorage.clear();
    window.location.href = 'login.html';
}

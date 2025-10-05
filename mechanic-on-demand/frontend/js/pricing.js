// Pricing JavaScript for Mechanic On Demand Application

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializePricing();
});

/**
 * Initialize pricing functionality
 */
function initializePricing() {
    // Initialize billing toggle
    initializeBillingToggle();
    
    // Load pricing plans
    loadPricingPlans();
    
    // Initialize contact modal
    initializeContactModal();
}

/**
 * Initialize billing toggle (Monthly/Yearly)
 */
function initializeBillingToggle() {
    const monthlyRadio = document.getElementById('monthly');
    const yearlyRadio = document.getElementById('yearly');
    
    if (monthlyRadio && yearlyRadio) {
        monthlyRadio.addEventListener('change', () => updatePricingDisplay('monthly'));
        yearlyRadio.addEventListener('change', () => updatePricingDisplay('yearly'));
    }
}

/**
 * Update pricing display based on billing period
 */
function updatePricingDisplay(period) {
    const monthlyPrices = document.querySelectorAll('.monthly-price');
    const yearlyPrices = document.querySelectorAll('.yearly-price');
    const monthlyFeatures = document.querySelectorAll('.monthly-feature');
    const yearlyFeatures = document.querySelectorAll('.yearly-feature');
    
    if (period === 'monthly') {
        // Show monthly prices
        monthlyPrices.forEach(price => price.classList.remove('d-none'));
        yearlyPrices.forEach(price => price.classList.add('d-none'));
        
        // Show monthly features
        monthlyFeatures.forEach(feature => feature.classList.remove('d-none'));
        yearlyFeatures.forEach(feature => feature.classList.add('d-none'));
        
        // Update pricing cards
        updatePricingCards('monthly');
        
    } else if (period === 'yearly') {
        // Show yearly prices
        monthlyPrices.forEach(price => price.classList.add('d-none'));
        yearlyPrices.forEach(price => price.classList.remove('d-none'));
        
        // Show yearly features
        monthlyFeatures.forEach(feature => feature.classList.add('d-none'));
        yearlyFeatures.forEach(feature => feature.classList.remove('d-none'));
        
        // Update pricing cards
        updatePricingCards('yearly');
    }
}

/**
 * Update pricing cards with dynamic content
 */
function updatePricingCards(period) {
    const cards = document.querySelectorAll('.pricing-card');
    
    cards.forEach((card, index) => {
        const priceElement = card.querySelector('.pricing-price .display-4');
        const featuresList = card.querySelectorAll('.list-unstyled li');
        
        if (period === 'yearly') {
            // Apply yearly discount
            if (index === 0) { // Basic plan
                priceElement.textContent = '$299.99';
            } else if (index === 1) { // Premium plan
                priceElement.textContent = '$499.99';
            }
            
            // Update features to show yearly benefits
            featuresList.forEach(feature => {
                const text = feature.textContent;
                if (text.includes('5 bookings per month')) {
                    feature.innerHTML = '<i class="fas fa-check text-success me-2"></i>5 bookings per month <span class="badge bg-success ms-1">Save 10%</span>';
                } else if (text.includes('Unlimited bookings')) {
                    feature.innerHTML = '<i class="fas fa-check text-success me-2"></i>Unlimited bookings <span class="badge bg-success ms-1">Save 20%</span>';
                }
            });
            
        } else {
            // Reset to monthly pricing
            if (index === 0) { // Basic plan
                priceElement.textContent = '$29.99';
            } else if (index === 1) { // Premium plan
                priceElement.textContent = '$49.99';
            }
            
            // Reset features
            featuresList.forEach(feature => {
                const text = feature.textContent;
                if (text.includes('5 bookings per month')) {
                    feature.innerHTML = '<i class="fas fa-check text-success me-2"></i>5 bookings per month';
                } else if (text.includes('Unlimited bookings')) {
                    feature.innerHTML = '<i class="fas fa-check text-success me-2"></i>Unlimited bookings';
                }
            });
        }
    });
}

/**
 * Load pricing plans from API
 */
async function loadPricingPlans() {
    try {
        const response = await makeApiRequest('/pricing/plans');
        updatePricingPlansUI(response);
    } catch (error) {
        console.error('Error loading pricing plans:', error);
        // Use default pricing if API fails
        showDefaultPricing();
    }
}

/**
 * Update pricing plans UI with API data
 */
function updatePricingPlansUI(plans) {
    const pricingCards = document.querySelectorAll('.pricing-card');
    
    plans.forEach((plan, index) => {
        if (index < pricingCards.length) {
            const card = pricingCards[index];
            const priceElement = card.querySelector('.pricing-price .display-4');
            const descriptionElement = card.querySelector('.pricing-price + p');
            
            if (priceElement) {
                priceElement.textContent = `$${plan.price}`;
            }
            
            if (descriptionElement) {
                descriptionElement.textContent = plan.description;
            }
            
            // Update features if available
            if (plan.features) {
                try {
                    const features = JSON.parse(plan.features);
                    const featuresList = card.querySelector('.list-unstyled');
                    
                    if (featuresList && Array.isArray(features)) {
                        featuresList.innerHTML = features.map(feature => 
                            `<li class="mb-2"><i class="fas fa-check text-success me-2"></i>${feature}</li>`
                        ).join('');
                    }
                } catch (e) {
                    console.error('Error parsing features:', e);
                }
            }
        }
    });
}

/**
 * Show default pricing when API is not available
 */
function showDefaultPricing() {
    // This function is called when the API is not available
    // The default pricing is already in the HTML
    console.log('Using default pricing data');
}

/**
 * Initialize contact modal
 */
function initializeContactModal() {
    const contactModal = document.getElementById('contactModal');
    if (contactModal) {
        const sendButton = contactModal.querySelector('.btn-primary');
        if (sendButton) {
            sendButton.addEventListener('click', handleContactForm);
        }
    }
}

/**
 * Handle contact form submission
 */
function handleContactForm() {
    const email = document.getElementById('contactEmail').value;
    const message = document.getElementById('contactMessage').value;
    
    if (!email || !message) {
        showAlert('Please fill in all fields.', 'danger');
        return;
    }
    
    if (!isValidEmail(email)) {
        showAlert('Please enter a valid email address.', 'danger');
        return;
    }
    
    // Simulate sending message
    showAlert('Thank you for your interest! We will contact you soon.', 'success');
    
    // Close modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('contactModal'));
    modal.hide();
    
    // Clear form
    document.getElementById('contactEmail').value = '';
    document.getElementById('contactMessage').value = '';
}

/**
 * Calculate savings for yearly plans
 */
function calculateYearlySavings(monthlyPrice) {
    const yearlyPrice = monthlyPrice * 12;
    const discountedYearlyPrice = yearlyPrice * 0.8; // 20% discount
    const savings = yearlyPrice - discountedYearlyPrice;
    
    return {
        yearlyPrice: discountedYearlyPrice,
        savings: savings,
        savingsPercentage: 20
    };
}

/**
 * Animate pricing cards on scroll
 */
function animatePricingCards() {
    const cards = document.querySelectorAll('.pricing-card');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach((entry, index) => {
            if (entry.isIntersecting) {
                setTimeout(() => {
                    entry.target.classList.add('fade-in');
                }, index * 200);
            }
        });
    }, {
        threshold: 0.1
    });
    
    cards.forEach(card => {
        observer.observe(card);
    });
}

/**
 * Initialize pricing comparison table
 */
function initializePricingComparison() {
    const table = document.querySelector('.table');
    if (table) {
        // Add hover effects to table rows
        const rows = table.querySelectorAll('tbody tr');
        rows.forEach(row => {
            row.addEventListener('mouseenter', function() {
                this.style.backgroundColor = 'rgba(13, 110, 253, 0.05)';
            });
            
            row.addEventListener('mouseleave', function() {
                this.style.backgroundColor = '';
            });
        });
    }
}

/**
 * Initialize FAQ accordion
 */
function initializeFAQ() {
    const accordionItems = document.querySelectorAll('.accordion-item');
    
    accordionItems.forEach((item, index) => {
        const button = item.querySelector('.accordion-button');
        const collapse = item.querySelector('.accordion-collapse');
        
        if (button && collapse) {
            button.addEventListener('click', function() {
                // Add animation class
                collapse.classList.add('fade-in');
            });
        }
    });
}

// Initialize additional features when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    animatePricingCards();
    initializePricingComparison();
    initializeFAQ();
});

// Make functions globally available
window.Pricing = {
    updatePricingDisplay,
    calculateYearlySavings,
    loadPricingPlans
};
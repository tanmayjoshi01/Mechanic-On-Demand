package com.mechanicondemand.service;

import com.mechanicondemand.dto.BookingRequest;
import com.mechanicondemand.dto.BookingResponse;
import com.mechanicondemand.entity.*;
import com.mechanicondemand.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Booking Service
 *
 * Business logic layer for booking operations.
 * Handles booking creation, status updates, rating system, and booking management.
 */
@Service
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final MechanicRepository mechanicRepository;
    private final ServiceRepository serviceRepository;
    private final NotificationService notificationService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                         CustomerRepository customerRepository,
                         MechanicRepository mechanicRepository,
                         ServiceRepository serviceRepository,
                         NotificationService notificationService) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.mechanicRepository = mechanicRepository;
        this.serviceRepository = serviceRepository;
        this.notificationService = notificationService;
    }

    /**
     * Create a new booking
     *
     * @param customerEmail Customer email
     * @param bookingRequest Booking request data
     * @return Booking response with details
     */
    public BookingResponse createBooking(String customerEmail, BookingRequest bookingRequest) {
        // Find customer
        Customer customer = customerRepository.findByEmail(customerEmail)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Find mechanic
        Mechanic mechanic = mechanicRepository.findById(bookingRequest.getMechanicId())
            .orElseThrow(() -> new RuntimeException("Mechanic not found"));

        // Check if mechanic is available
        if (!mechanic.getIsAvailable()) {
            throw new RuntimeException("Mechanic is not currently available");
        }

        // Find service
        Service service = serviceRepository.findById(bookingRequest.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service not found"));

        // Create booking
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setMechanic(mechanic);
        booking.setService(service);
        booking.setScheduledDateTime(bookingRequest.getScheduledDateTime());
        booking.setCustomerAddress(bookingRequest.getCustomerAddress());
        booking.setCustomerLatitude(bookingRequest.getCustomerLatitude());
        booking.setCustomerLongitude(bookingRequest.getCustomerLongitude());

        // Calculate total price
        BigDecimal totalPrice = calculateTotalPrice(service, bookingRequest.getEstimatedDuration());
        booking.setTotalPrice(totalPrice);
        booking.setEstimatedDurationHours(bookingRequest.getEstimatedDuration());

        // Save booking
        Booking savedBooking = bookingRepository.save(booking);

        // Send notification to mechanic
        notificationService.sendBookingRequestNotification(mechanic, customer, savedBooking);

        // Convert to response DTO
        return convertToBookingResponse(savedBooking);
    }

    /**
     * Get bookings for a user (customer or mechanic)
     *
     * @param userEmail User email
     * @param status Optional status filter
     * @param page Page number
     * @param size Page size
     * @return List of booking responses
     */
    public List<BookingResponse> getBookingsForUser(String userEmail, String status, int page, int size) {
        // Find user
        User user = customerRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            user = mechanicRepository.findByEmail(userEmail).orElse(null);
        }

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Get bookings based on user type
        List<Booking> bookings;
        if (user instanceof Customer) {
            bookings = bookingRepository.findByCustomerId(user.getId());
        } else {
            bookings = bookingRepository.findByMechanicId(user.getId());
        }

        // Filter by status if provided
        if (status != null) {
            BookingStatus bookingStatus = BookingStatus.valueOf(status.toUpperCase());
            bookings = bookings.stream()
                .filter(booking -> booking.getStatus() == bookingStatus)
                .collect(Collectors.toList());
        }

        // Convert to response DTOs
        return bookings.stream()
            .map(this::convertToBookingResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get booking by ID (with access control)
     *
     * @param userEmail User email
     * @param bookingId Booking ID
     * @return Booking response or null if not found/accessible
     */
    public BookingResponse getBookingById(String userEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return null;
        }

        // Check if user has access to this booking
        boolean hasAccess = booking.getCustomer().getEmail().equals(userEmail) ||
                           booking.getMechanic().getEmail().equals(userEmail);

        if (!hasAccess) {
            return null;
        }

        return convertToBookingResponse(booking);
    }

    /**
     * Update booking status (mechanic only)
     *
     * @param mechanicEmail Mechanic email
     * @param bookingId Booking ID
     * @param newStatus New status
     * @return Success message
     */
    public String updateBookingStatus(String mechanicEmail, Long bookingId, String newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Verify mechanic owns this booking
        if (!booking.getMechanic().getEmail().equals(mechanicEmail)) {
            throw new RuntimeException("Unauthorized to update this booking");
        }

        BookingStatus status = BookingStatus.valueOf(newStatus.toUpperCase());
        booking.setStatus(status);
        bookingRepository.save(booking);

        // Send notification to customer
        notificationService.sendBookingStatusNotification(booking.getCustomer(), booking);

        return "Booking status updated to " + status;
    }

    /**
     * Rate a completed booking
     *
     * @param userEmail User email (customer or mechanic)
     * @param bookingId Booking ID
     * @param rating Rating (1-5)
     * @param comment Optional comment
     * @return Success message
     */
    public String rateBooking(String userEmail, Long bookingId, Integer rating, String comment) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Verify user is part of this booking and booking is completed
        boolean isCustomer = booking.getCustomer().getEmail().equals(userEmail);
        boolean isMechanic = booking.getMechanic().getEmail().equals(userEmail);

        if (!isCustomer && !isMechanic) {
            throw new RuntimeException("Unauthorized to rate this booking");
        }

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new RuntimeException("Can only rate completed bookings");
        }

        // Update appropriate rating
        if (isCustomer) {
            booking.setMechanicRating(rating);
            // Update mechanic's overall rating
            booking.getMechanic().updateRating(rating);
            mechanicRepository.save(booking.getMechanic());
        } else {
            booking.setCustomerRating(rating);
        }

        bookingRepository.save(booking);

        return "Rating submitted successfully";
    }

    /**
     * Cancel booking
     *
     * @param userEmail User email
     * @param bookingId Booking ID
     * @return Success message
     */
    public String cancelBooking(String userEmail, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if user can cancel this booking
        boolean canCancel = booking.getCustomer().getEmail().equals(userEmail) ||
                           booking.getMechanic().getEmail().equals(userEmail);

        if (!canCancel) {
            throw new RuntimeException("Unauthorized to cancel this booking");
        }

        // Check if booking can be cancelled (not too close to scheduled time)
        if (booking.getScheduledDateTime().isBefore(java.time.LocalDateTime.now().plusHours(2))) {
            throw new RuntimeException("Cannot cancel booking within 2 hours of scheduled time");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Send cancellation notification
        if (booking.getCustomer().getEmail().equals(userEmail)) {
            notificationService.sendBookingCancelledNotification(booking.getMechanic(), booking);
        } else {
            notificationService.sendBookingCancelledNotification(booking.getCustomer(), booking);
        }

        return "Booking cancelled successfully";
    }

    /**
     * Calculate total price for booking
     *
     * @param service Service being booked
     * @param estimatedDuration Estimated duration in hours
     * @return Total price
     */
    private BigDecimal calculateTotalPrice(Service service, BigDecimal estimatedDuration) {
        BigDecimal totalPrice = service.getBasePrice();

        if (service.getHourlyRate() != null && estimatedDuration != null) {
            totalPrice = totalPrice.add(service.getHourlyRate().multiply(estimatedDuration));
        }

        return totalPrice;
    }

    /**
     * Convert Booking entity to BookingResponse DTO
     *
     * @param booking Booking entity
     * @return BookingResponse DTO
     */
    private BookingResponse convertToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setStatus(booking.getStatus());
        response.setScheduledDateTime(booking.getScheduledDateTime());
        response.setTotalPrice(booking.getTotalPrice());
        response.setCustomerName(booking.getCustomer().getFullName());
        response.setMechanicName(booking.getMechanic().getFullName());
        response.setServiceName(booking.getService().getName());
        response.setCustomerAddress(booking.getCustomerAddress());
        response.setMechanicNotes(booking.getMechanicNotes());
        response.setCustomerRating(booking.getCustomerRating());
        response.setMechanicRating(booking.getMechanicRating());
        response.setCreatedAt(booking.getCreatedAt());

        return response;
    }
}
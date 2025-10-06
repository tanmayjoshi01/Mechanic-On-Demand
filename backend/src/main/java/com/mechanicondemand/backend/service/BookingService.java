package com.mechanicondemand.backend.service;

import com.mechanicondemand.backend.dto.BookingDtos;
import com.mechanicondemand.backend.model.Booking;
import com.mechanicondemand.backend.model.User;
import com.mechanicondemand.backend.repository.BookingRepository;
import com.mechanicondemand.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.mechanicondemand.backend.notification.BookingNotifier;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BookingNotifier bookingNotifier;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, BookingNotifier bookingNotifier) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.bookingNotifier = bookingNotifier;
    }

    public Booking createBooking(Long customerId, BookingDtos.CreateBookingRequest request) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setLatitude(request.latitude);
        booking.setLongitude(request.longitude);
        booking.setIssueDescription(request.issueDescription);
        Booking saved = bookingRepository.save(booking);
        if (saved.getMechanic() != null) {
            bookingNotifier.notifyMechanic(saved.getMechanic().getId(), saved);
        }
        return saved;
    }

    public Booking assignMechanic(Long bookingId, Long mechanicId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));
        booking.setMechanic(mechanic);
        Booking saved = bookingRepository.save(booking);
        bookingNotifier.notifyMechanic(mechanic.getId(), saved);
        return saved;
    }

    public Booking updateStatus(Long bookingId, Booking.Status status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        booking.setStatus(status);
        if (status == Booking.Status.ACCEPTED) {
            booking.setAcceptedAt(Instant.now());
        }
        if (status == Booking.Status.COMPLETED) {
            booking.setCompletedAt(Instant.now());
        }
        Booking saved = bookingRepository.save(booking);
        if (saved.getMechanic() != null) {
            bookingNotifier.notifyMechanic(saved.getMechanic().getId(), saved);
        }
        return saved;
    }

    public List<Booking> listForCustomer(Long customerId) {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return bookingRepository.findByCustomer(customer);
    }

    public List<Booking> listForMechanic(Long mechanicId) {
        User mechanic = userRepository.findById(mechanicId)
                .orElseThrow(() -> new IllegalArgumentException("Mechanic not found"));
        return bookingRepository.findByMechanic(mechanic);
    }
}

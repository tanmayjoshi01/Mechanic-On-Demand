package com.mechanicondemand.backend.controller;

import com.mechanicondemand.backend.dto.BookingDtos;
import com.mechanicondemand.backend.model.Booking;
import com.mechanicondemand.backend.service.BookingService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    private Long authenticatedUserId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<BookingDtos.BookingResponse> create(@Valid @RequestBody BookingDtos.CreateBookingRequest request,
                                                              Authentication authentication) {
        Booking booking = bookingService.createBooking(authenticatedUserId(authentication), request);
        return ResponseEntity.ok(toDto(booking));
    }

    @GetMapping("/me")
    public ResponseEntity<List<BookingDtos.BookingResponse>> myBookings(Authentication authentication) {
        List<Booking> list = bookingService.listForCustomer(authenticatedUserId(authentication));
        return ResponseEntity.ok(list.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<BookingDtos.BookingResponse>> assigned(Authentication authentication) {
        List<Booking> list = bookingService.listForMechanic(authenticatedUserId(authentication));
        return ResponseEntity.ok(list.stream().map(this::toDto).collect(Collectors.toList()));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BookingDtos.BookingResponse> updateStatus(@PathVariable Long id,
                                                                    @RequestBody BookingDtos.UpdateStatusRequest request) {
        Booking booking = bookingService.updateStatus(id, Booking.Status.valueOf(request.status.toUpperCase()));
        return ResponseEntity.ok(toDto(booking));
    }

    @PutMapping("/{id}/assign/{mechanicId}")
    public ResponseEntity<BookingDtos.BookingResponse> assign(@PathVariable Long id, @PathVariable Long mechanicId) {
        Booking booking = bookingService.assignMechanic(id, mechanicId);
        return ResponseEntity.ok(toDto(booking));
    }

    private BookingDtos.BookingResponse toDto(Booking booking) {
        BookingDtos.BookingResponse dto = new BookingDtos.BookingResponse();
        dto.id = booking.getId();
        dto.customerId = booking.getCustomer() != null ? booking.getCustomer().getId() : null;
        dto.mechanicId = booking.getMechanic() != null ? booking.getMechanic().getId() : null;
        dto.status = booking.getStatus().name();
        dto.latitude = booking.getLatitude();
        dto.longitude = booking.getLongitude();
        dto.issueDescription = booking.getIssueDescription();
        dto.createdAt = booking.getCreatedAt();
        dto.acceptedAt = booking.getAcceptedAt();
        dto.completedAt = booking.getCompletedAt();
        return dto;
    }
}

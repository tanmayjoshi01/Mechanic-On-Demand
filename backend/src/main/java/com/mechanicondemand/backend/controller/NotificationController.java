package com.mechanicondemand.backend.controller;

import com.mechanicondemand.backend.notification.BookingNotifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final BookingNotifier bookingNotifier;

    public NotificationController(BookingNotifier bookingNotifier) {
        this.bookingNotifier = bookingNotifier;
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(Authentication authentication) {
        Long mechanicId = Long.parseLong(authentication.getName());
        return bookingNotifier.subscribe(mechanicId);
    }
}

package com.mechanicondemand.backend.notification;

import com.mechanicondemand.backend.model.Booking;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookingNotifier {
    private final Map<Long, SseEmitter> mechanicEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long mechanicId) {
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        mechanicEmitters.put(mechanicId, emitter);
        emitter.onCompletion(() -> mechanicEmitters.remove(mechanicId));
        emitter.onTimeout(() -> mechanicEmitters.remove(mechanicId));
        return emitter;
    }

    public void notifyMechanic(Long mechanicId, Booking booking) {
        SseEmitter emitter = mechanicEmitters.get(mechanicId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("booking")
                        .data(Map.of(
                                "bookingId", booking.getId(),
                                "status", booking.getStatus().name(),
                                "latitude", booking.getLatitude(),
                                "longitude", booking.getLongitude(),
                                "issueDescription", booking.getIssueDescription()
                        )));
            } catch (IOException e) {
                emitter.completeWithError(e);
                mechanicEmitters.remove(mechanicId);
            }
        }
    }
}

package com.mechanicondemand.backend.dto;

import java.time.Instant;

public class BookingDtos {
    public static class CreateBookingRequest {
        public Double latitude;
        public Double longitude;
        public String issueDescription;
    }

    public static class UpdateStatusRequest {
        public String status; // ACCEPTED, REJECTED, COMPLETED, CANCELLED
    }

    public static class BookingResponse {
        public Long id;
        public Long customerId;
        public Long mechanicId;
        public String status;
        public Double latitude;
        public Double longitude;
        public String issueDescription;
        public Instant createdAt;
        public Instant acceptedAt;
        public Instant completedAt;
    }
}

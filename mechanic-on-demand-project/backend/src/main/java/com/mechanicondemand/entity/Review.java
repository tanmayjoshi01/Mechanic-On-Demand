package com.mechanicondemand.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Review Entity
 *
 * Represents reviews and ratings left by customers for mechanics and vice versa.
 * Supports bidirectional reviews between customers and mechanics.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "reviewee_id", nullable = false)
    private User reviewee;

    @Column(nullable = false)
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @Column
    @Size(max = 1000, message = "Comment must not exceed 1000 characters")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Booking booking, User reviewer, User reviewee, Integer rating) {
        this();
        this.booking = booking;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.rating = rating;
    }

    public Review(Booking booking, User reviewer, User reviewee, Integer rating, String comment) {
        this(booking, reviewer, reviewee, rating);
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public void setReviewee(User reviewee) {
        this.reviewee = reviewee;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public boolean isPositiveReview() {
        return rating != null && rating >= 4;
    }

    public boolean isNegativeReview() {
        return rating != null && rating <= 2;
    }
}
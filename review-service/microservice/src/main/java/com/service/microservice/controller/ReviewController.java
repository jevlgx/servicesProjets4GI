package com.service.microservice.controller;

import com.service.microservice.model.DriverReview;
import com.service.microservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/create")
    public ResponseEntity<DriverReview> createReview(@RequestBody DriverReview review) {
        DriverReview createdReview = reviewService.createReview(review);
        return ResponseEntity.ok(createdReview);
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<DriverReview>> getReviewsByDriver(@PathVariable UUID driverId) {
        List<DriverReview> reviews = reviewService.getReviewsByDriver(driverId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<DriverReview> getReviewByReservation(@PathVariable UUID reservationId) {
        DriverReview review = reviewService.getReviewByReservation(reservationId);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<DriverReview> updateReview(@PathVariable UUID reviewId, @RequestHeader("User-Id") UUID userId, @RequestBody DriverReview review) {
        DriverReview updatedReview = reviewService.updateReview(reviewId, userId,review);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId, @RequestHeader("User-Id") UUID userId) {
        reviewService.deleteReview(reviewId,userId);
        return ResponseEntity.noContent().build();
    }
}
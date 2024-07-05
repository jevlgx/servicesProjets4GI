package com.service.microservice.service;


import com.service.microservice.exceptions.ResourceNotFoundException;
import com.service.microservice.exceptions.UnauthorizedException;
import com.service.microservice.model.AverageRating;
import com.service.microservice.model.DriverReview;
import com.service.microservice.repository.AverageRatingRepository;
import com.service.microservice.repository.DriverReviewRepository;
import com.service.microservice.utils.CassandraIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {
    private final DriverReviewRepository driverReviewRepository;
    private final AverageRatingRepository averageRatingRepository;

   /* private final DriverServiceClient driverServiceClient;
    private final ReservationServiceClient reservationServiceClient;*/
    private final CassandraIdGenerator cassandraIdGenerator;

    @Autowired
    public ReviewService(DriverReviewRepository driverReviewRepository,
                         AverageRatingRepository averageRatingRepository,

                        /* DriverServiceClient driverServiceClient,
                         ReservationServiceClient reservationServiceClient,*/
                         CassandraIdGenerator cassandraIdGenerator) {
        this.driverReviewRepository = driverReviewRepository;
        this.averageRatingRepository = averageRatingRepository;
      /*  this.driverServiceClient = driverServiceClient;
        this.reservationServiceClient = reservationServiceClient;*/
        this.cassandraIdGenerator = cassandraIdGenerator;
    }

    public DriverReview createReview(DriverReview review) {



       /* if (!driverServiceClient.userExists(review.getUserId())) {
            throw new IllegalArgumentException("User does not exist");
        }*/

        // Vérifier si la réservation existe et est complétée
       /* if (!reservationServiceClient.isReservationCompletedForUserAndDriver(review.getReservationId(), review.getUserId(), review.getDriverId())) {
            throw new IllegalArgumentException("Invalid reservation or not completed");
        }*/

        // Générer un ID unique pour la review
        UUID reviewId = cassandraIdGenerator.getNextId();
        review.setReviewId(reviewId);

        // Sauvegarder la review
        DriverReview savedReview = driverReviewRepository.save(review);

        // Mettre à jour la moyenne des notes
        updateAverageRating(review.getDriverId());

        return savedReview;
    }

    private void updateAverageRating(UUID driverId) {
        List<DriverReview> reviews = driverReviewRepository.findByDriverId(driverId);
        BigDecimal averageRating = calculateAverageRating(reviews);
        int totalReviews = reviews.size();

        AverageRating avgRating = new AverageRating();
        avgRating.setDriverId(driverId);
        avgRating.setAverageRating(averageRating);
        avgRating.setTotalReviews(totalReviews);
        avgRating.setUpdatedAt(LocalDateTime.now());

        averageRatingRepository.save(avgRating);
    }

    private BigDecimal calculateAverageRating(List<DriverReview> reviews) {
        // Implémenter la logique de calcul du score moyen
        return BigDecimal.valueOf(0);
    }

    public List<DriverReview> getReviewsByDriver(UUID driverId) {
        return driverReviewRepository.findByDriverId(driverId);
    }

    public DriverReview getReviewByReservation(UUID reservationId) {
        return driverReviewRepository.findByReservationId(reservationId);
    }


    public DriverReview updateReview(UUID reviewId,UUID userId, DriverReview updatedReview) {
        DriverReview existingReview = driverReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (!existingReview.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to update this review");
        }
        // Mise à jour des champs modifiables
        existingReview.setComment(updatedReview.getComment());
        existingReview.setNote(updatedReview.getNote());
        existingReview.setUpdatedAt(LocalDateTime.now());
        existingReview.setIcon(updatedReview.getIcon());

        DriverReview savedReview = driverReviewRepository.save(existingReview);

        // Mettre à jour la moyenne des notes
        updateAverageRating(existingReview.getDriverId());

        return savedReview;
    }

    public void deleteReview(UUID reviewId,UUID userId) {
        DriverReview review = driverReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));


        if (!review.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to delete this review");
        }
        driverReviewRepository.delete(review);

        // Mettre à jour la moyenne des notes
        updateAverageRating(review.getDriverId());
    }

}
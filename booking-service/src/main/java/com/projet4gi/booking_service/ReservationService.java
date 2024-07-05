package com.projet4gi.booking_service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projet4gi.booking_service.models.Reservation;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Enregistrer une nouvelle reservation
    public Reservation createReservation(UUID userId, UUID driverId, UUID planningId) {
        Reservation reservation = new Reservation();
        reservation.setReservationId(UUID.randomUUID());
        reservation.setUserId(userId);
        reservation.setDriverId(driverId);
        reservation.setPlanningId(planningId);
        reservation.setReservationDate(Instant.now());

        reservation.setAnnouncementId(null);
        reservation.setCreatedFor(null);
        reservation.setEndFor(null);
        reservation.setTarif(null);
        reservation.setPaymentId(null);
        reservation.setState("PENDING_PAYMENT");

        return reservationRepository.save(reservation);
    }

    // Recuperer une reservation à partir de son identifiant
    public Reservation getReservationById(UUID reservationId){
        return reservationRepository.findByReservationId(reservationId);
    }

    // Recuperer toutes les reservations effectuées par un utilisateur

    // Recuperer toutes les reservations d'un conducteur
}
package com.projet4gi.booking_service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.projet4gi.booking_service.models.Reservation;
import com.projet4gi.booking_service.models.ReservationDto;
import com.projet4gi.exceptions.ApplicationException;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    // Enregistrer une nouvelle reservation
    public Reservation createReservation(ReservationDto reservationDto) {
        Reservation reservation = Reservation.builder()
            .id(UUID.randomUUID())
            .userId(reservationDto.getUserId())
            .driverId(reservationDto.getDriverId())
            .planningId(reservationDto.getPlanningId())
            .reservationDate(Instant.now())
            .announcementId(null)
            .createdFor(null)
            .endFor(null)
            .tarif(null)
            .paymentId(null)
            .state("PENDING_PAYMENT")
            .build();
        return reservationRepository.save(reservation);
    }

    /* // Recuperer une reservation à partir de son identifiant
    public Reservation getReservationById(UUID reservationId){
        return reservationRepository.findById(reservationId);
    } */

    //recuperer les reservations faites par un utilisateur
    public List<Reservation> getUserReservations(UUID userId){
        List<Reservation> reservations = reservationRepository.findAllByUserId(userId).orElseThrow(
            ()->  new ApplicationException("Auccune reservation trouvée", HttpStatus.NOT_FOUND)
        );
        return reservations;
    }

    // Recuperer toutes les reservations d'un conducteur
    public List<Reservation> getDriverReservations(UUID driverId){
        List<Reservation> reservations = reservationRepository.findAllByDriverId(driverId).orElseThrow(
            ()->  new ApplicationException("Auccune reservation trouvée", HttpStatus.NOT_FOUND)
        );
        return reservations;
    }
}
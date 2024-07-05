package com.projet4gi.booking_service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projet4gi.booking_service.models.Reservation;
import com.projet4gi.booking_service.models.ReservationDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create-reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDto reservationDto) {

        Reservation createdReservation = reservationService.createReservation(
            reservationDto.getUserId(),
            reservationDto.getDriverId(),
            reservationDto.getPlanningId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @GetMapping("/getUserReservationsById")
    public ResponseEntity<Reservation> getReservationById(@RequestParam UUID reservationId) {
        try {
            Reservation reservation = reservationService.getReservationById(reservationId);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null);
        }
    }
    
}
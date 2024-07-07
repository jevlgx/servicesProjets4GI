package com.projet4gi.booking_service;

import java.util.List;
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

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ReservationController {

    private ReservationService reservationService;

    @PostMapping("/create-reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationDto reservationDto) {
        Reservation createdReservation = reservationService.createReservation(reservationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @GetMapping("/getUserReservations")
    public ResponseEntity<List<Reservation>> getUserReservationsByUserId(@RequestParam UUID userId) {
        List<Reservation> reservations = reservationService.getUserReservations(userId);
        return ResponseEntity.status(HttpStatus.FOUND).body(reservations);
    }
    
    @GetMapping("/getDriverReservations")
    public ResponseEntity<List<Reservation>> getDriverReservationsByDriverId(@RequestParam UUID driverId) {
        List<Reservation> reservations = reservationService.getDriverReservations(driverId);
        return ResponseEntity.status(HttpStatus.FOUND).body(reservations);
    }
}
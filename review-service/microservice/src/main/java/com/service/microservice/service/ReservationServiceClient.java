package com.service.microservice.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

//lLOGIQUE DE COMMUNICATION AVEC LE BOOKING SERVICE
public class ReservationServiceClient {
    public boolean isReservationCompletedForUserAndDriver(UUID reservationId, UUID userId, UUID driverId) {
        return true;
    }
}
//@FeignClient(name = "reservation-service")
//public interface ReservationServiceClient {
//    @GetMapping("/api/reservations/{reservationId}/completed")
//    boolean isReservationCompletedForUserAndDriver(@PathVariable UUID reservationId, @PathVariable UUID userId, @PathVariable UUID driverId);
//}

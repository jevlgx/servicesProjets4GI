package com.projet4gi.booking_service;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.projet4gi.booking_service.models.Reservation;

@Repository
public interface ReservationRepository extends CassandraRepository<Reservation, UUID> {
    Reservation findByReservationId(UUID reservationId);
}

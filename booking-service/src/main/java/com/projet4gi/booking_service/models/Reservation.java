package com.projet4gi.booking_service.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Table("reservations")
public class Reservation {
    @PrimaryKey
    private UUID id;

    @Column("userid")
    private UUID userId;

    @Column("driverid")
    private UUID driverId;

    @Column("announcementid")
    private UUID announcementId;

    @Column("planningid")
    private UUID planningId;

    @Column("reservationdate")
    private Instant reservationDate;

    @Column("createdfor")
    private Instant createdFor;

    @Column("endfor")
    private Instant endFor;

    @Column("tarif")
    private BigDecimal tarif;

    @Column("paymentid")
    private UUID paymentId;

    @Column("state")
    private String state;

    // Getters and setters
}
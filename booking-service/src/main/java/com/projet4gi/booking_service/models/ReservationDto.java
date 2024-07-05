package com.projet4gi.booking_service.models;

import java.util.UUID;

import lombok.Data;

@Data
public class ReservationDto {
    private UUID userId;
    private UUID driverId;
    private UUID planningId;
}
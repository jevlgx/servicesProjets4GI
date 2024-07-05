package com.service.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("vehicles")
public class Vehicle {
    @PrimaryKey
    private String vehicleId;//IMMATRICULATION PAR EXEMPLE
    private String VehiclePictureUrl;
    private String fueltype;
    private String boxtype;
    private String PictureDescription;//title
    private String model;
    private String brand;

}

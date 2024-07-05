package com.service.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDTO {

    //USER
    private UUID userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;//LOCATION
    private String password;
    private String role;
    private String sex;
    private LocalDate birthday;
    private LocalDateTime createdAt=null;
    private LocalDateTime updatedAt=null;
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean smsNotifications;
    private String userPictureUrl;

    //DRIVER
    private UUID driverId;
    private int experiences;
    private String driverLicense;
    private String interests;
    private String state;
    private String profile;
    private Set<String> zones;
    private String cvUrl;

    //VEHICLE
    private String vehicleID;
    private String VehiclePictureUrl;
    private String PictureDescription;
    private String fueltype;
    private String boxtype;
    private String model;
    private String brand;


}

package com.service.microservice.repository;

import com.service.microservice.model.Driver;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DriverRepository extends CassandraRepository<Driver, UUID> {

    @Query("SELECT * FROM drivers WHERE userId = ?0 ALLOW FILTERING")
    Driver findByUserId(UUID userId);

    @Query("UPDATE drivers SET userId = ?1, vehicleId = ?2, experiences = ?3, driverLicense = ?4, interests = ?5, " +
            "createdAt = ?6, updatedAt = ?7, state = ?8, profile = ?9, cvUrl = ?10, VehiclePictureUrl = ?11, " +
            "PictureDescription = ?12, zones = ?13 WHERE driverId = ?0")
    void updateDriver(UUID driverId, UUID userId, String vehicleId, int experiences, String driverLicense,
                      String interests, LocalDateTime createdAt, LocalDateTime updatedAt, String state,
                      String profile, String cvUrl,
                      Set<String> zones);

}
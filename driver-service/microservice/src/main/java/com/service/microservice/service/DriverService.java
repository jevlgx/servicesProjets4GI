package com.service.microservice.service;

import com.service.microservice.model.Driver;
import com.service.microservice.repository.DriverRepository;
import com.service.microservice.utils.CassandraIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {

    @Autowired
    private CassandraIdGenerator cassandraIdGenerator;
    @Autowired
    private DriverRepository driverRepository;

    public Driver createDriver(Driver driver) {
        driver.setDriverId(cassandraIdGenerator.getNextId());
        return driverRepository.save(driver);
    }

    public Optional<Driver> getDriverById(UUID id) {
        return driverRepository.findById(id);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Driver saveDriver(Driver driver) {
        driver.setDriverId(cassandraIdGenerator.getNextId());
        return driverRepository.save(driver);
    }

    public Driver findByUserId(UUID userId) {
        return driverRepository.findByUserId(userId);
    }

    public void deleteDriver(UUID driverId) {
        driverRepository.deleteById(driverId);
    }

    public Driver updateDriver(Driver driver) {
        driverRepository.updateDriver(
                driver.getDriverId(),
                driver.getUserId(),
                driver.getVehicleId(),
                driver.getExperiences(),
                driver.getDriverLicense(),
                driver.getInterests(),
                driver.getCreatedAt(),
                driver.getUpdatedAt(),
                driver.getState(),
                driver.getProfile(),
                driver.getCvUrl(),
                driver.getZones()
        );

        return  driver;
    }
}
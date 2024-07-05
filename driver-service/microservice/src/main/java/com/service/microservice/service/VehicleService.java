package com.service.microservice.service;


import com.service.microservice.model.Driver;
import com.service.microservice.model.Vehicle;
import com.service.microservice.repository.VehicleRepository;
import com.service.microservice.utils.CassandraIdGenerator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VehicleService {
    private final CassandraIdGenerator cassandraIdGenerator;
    private final VehicleRepository vehicleRepository;

    public VehicleService(CassandraIdGenerator cassandraIdGenerator, VehicleRepository vehicleRepository) {
        this.cassandraIdGenerator = cassandraIdGenerator;
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findByVehicleId(String vehicleId) {
        return vehicleRepository.findById(vehicleId).orElse(null);
    }

    public Vehicle save(Vehicle vehicle) {
        vehicle.setVehicleId(cassandraIdGenerator.getNextId().toString());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(String vehicleId) {
        vehicleRepository.deleteById(vehicleId);
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicleRepository.updateVehicule(vehicle.getVehicleId(),vehicle.getVehiclePictureUrl(),vehicle.getFueltype(),
                vehicle.getBoxtype(), vehicle.getPictureDescription(), vehicle.getModel(),  vehicle.getBrand());
    }
}

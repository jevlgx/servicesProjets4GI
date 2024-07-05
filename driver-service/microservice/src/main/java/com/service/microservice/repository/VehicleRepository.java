package com.service.microservice.repository;


import com.service.microservice.model.Vehicle;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VehicleRepository extends CassandraRepository<Vehicle, String> {

    @Query("SELECT * FROM vehicles WHERE vehicleId = ?0 ALLOW FILTERING")
    Vehicle findByVehicleId(String vehicleId);

    @Query("UPDATE vehicles SET  VehiclePictureUrl=?1, fueltype =?2, boxtype =?3, PictureDescription = ?4, model=?5,brand=?6 WHERE vehicleId = ?0")
    void updateVehicule(String vehicleId, String VehiclePictureUrl, String fueltype, String boxtype, String pictureDescription, String model, String brand);


}
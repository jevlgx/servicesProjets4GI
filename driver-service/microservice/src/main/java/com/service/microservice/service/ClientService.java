package com.service.microservice.service;

import com.service.microservice.dto.ClientDTO;
import com.service.microservice.model.Driver;
import com.service.microservice.model.User;
import com.service.microservice.model.Vehicle;
import com.service.microservice.repository.DriverRepository;
import com.service.microservice.repository.UserRepository;
import com.service.microservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public ClientService(UserRepository userRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<ClientDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<ClientDTO> clientDTOs = new ArrayList<>();

        for (User user : users) {
            ClientDTO clientDTO = convertToClientDTO(user);

            if ("driver".equalsIgnoreCase(user.getRole())) {
                Driver driver = driverRepository.findByUserId(user.getUserId());
                if (driver != null) {
                    updateClientDTOWithDriverInfo(clientDTO, driver);
                    if(!"null".equalsIgnoreCase(driver.getVehicleId())){
                        Vehicle vehicle = vehicleRepository.findByVehicleId(driver.getVehicleId());
                        updateClientDTOWithVehicleDriverInfo(clientDTO, vehicle);

                    }
                }
            }

            clientDTOs.add(clientDTO);
        }

        return clientDTOs;
    }


    private ClientDTO convertToClientDTO(User user) {
        return ClientDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .password(user.getPassword())
                .role(user.getRole())
                .sex(user.getSex())
                .birthday(user.getBirthday())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .emailNotifications(user.getEmailNotifications())
                .pushNotifications(user.getPushNotifications())
                .smsNotifications(user.getSmsNotifications())
                .userPictureUrl(user.getUserPictureUrl())
                .build();
    }

    private void updateClientDTOWithDriverInfo(ClientDTO clientDTO, Driver driver) {
        clientDTO.setDriverId(driver.getDriverId());
        clientDTO.setExperiences(driver.getExperiences());
        clientDTO.setDriverLicense(driver.getDriverLicense());
        clientDTO.setInterests(driver.getInterests());
        clientDTO.setState(driver.getState());
        clientDTO.setProfile(driver.getProfile());
        clientDTO.setZones(driver.getZones());
        clientDTO.setCvUrl(driver.getCvUrl());

    }

    private void updateClientDTOWithVehicleDriverInfo(ClientDTO clientDTO, Vehicle vehicle) {
        clientDTO.setBoxtype(vehicle.getBoxtype());
        clientDTO.setModel(vehicle.getModel());
        clientDTO.setBrand(vehicle.getBrand());
        clientDTO.setFueltype(vehicle.getFueltype());
        clientDTO.setPictureDescription(vehicle.getPictureDescription());

    }
}

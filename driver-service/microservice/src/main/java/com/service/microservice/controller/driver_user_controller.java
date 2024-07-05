package com.service.microservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.service.microservice.dto.ClientDTO;
import com.service.microservice.model.Driver;
import com.service.microservice.model.User;
import com.service.microservice.model.Vehicle;
import com.service.microservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.http.HttpStatus;
//import com.service.microservice.dto.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, maxAge = 3600)
public class driver_user_controller {


    private final UserService userService;

    private final ClientService clientService;

    private final DriverService driverService;

    private final VehicleService vehicleService;

    private final FileStorageService fileStorageService;

    @Autowired
    public driver_user_controller(UserService userService, ClientService clientService, DriverService driverService, VehicleService vehicleService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.clientService = clientService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(value="/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDTO> createClient(
                                                  @RequestPart("clientDTO") String clientDTOJson,
                                                  @RequestPart(value = "userPicture", required = false) MultipartFile userPicture,
                                                  @RequestPart(value = "cv", required = false) MultipartFile cv,
                                                  @RequestPart(value = "vehiclePicture", required = false) MultipartFile vehiclePicture
                                                  ) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ClientDTO clientDTO = mapper.readValue(clientDTOJson, ClientDTO.class);

        if (userPicture != null) {
            String userPictureUrl = fileStorageService.storeFile(userPicture, "user_pictures");
            clientDTO.setUserPictureUrl(userPictureUrl);
        }

        User user = convertToUser(clientDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user = userService.saveUser(user);

        if ("driver".equalsIgnoreCase(clientDTO.getRole())) {

            if (cv != null) {
                String cvUrl = fileStorageService.storeFile(cv, "cvs");
                clientDTO.setCvUrl(cvUrl);
            }

            Driver driver = convertToDriver(clientDTO);
            driver.setUserId(user.getUserId());
            driver.setCreatedAt(user.getCreatedAt());
            driver.setUpdatedAt(user.getUpdatedAt());


            if (vehiclePicture != null) {
                    String vehiclePictureUrl = fileStorageService.storeFile(vehiclePicture, "vehicle_pictures");
                    clientDTO.setVehiclePictureUrl(vehiclePictureUrl);

                    Vehicle vehicle=extractVehicle(clientDTO);
                    vehicle=vehicleService.save(vehicle);
                    driver.setVehicleId(vehicle.getVehicleId());
            }

            else driver.setVehicleId("null");//conducteur sans vehicule
            driver=driverService.saveDriver(driver);
        }

        ClientDTO createdUser=convertToClientDTO(user);
        return ResponseEntity.ok(createdUser);

    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID userId) {
        Driver driver = driverService.findByUserId(userId);
        if (driver != null) {

            fileStorageService.deleteFile(driver.getCvUrl());

            Vehicle vehicle = vehicleService.findByVehicleId(driver.getVehicleId());
            if (vehicle != null) {
                fileStorageService.deleteFile(vehicle.getVehiclePictureUrl());
                vehicleService.deleteVehicle(vehicle.getVehicleId());
            }

            driverService.deleteDriver(driver.getDriverId());
        }
        User user = userService.findById(userId);
        fileStorageService.deleteFile(user.getUserPictureUrl());
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/update",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ClientDTO> updateClient(@RequestPart("clientDTO") String clientDTOJson,
                                                  @RequestPart(value = "userPicture", required = false) MultipartFile userPicture,
                                                  @RequestPart(value = "cv", required = false) MultipartFile cv,
                                                  @RequestPart(value = "vehiclePicture", required = false) MultipartFile vehiclePicture) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ClientDTO clientDTO = mapper.readValue(clientDTOJson, ClientDTO.class);
        if (userPicture != null) {
            String userPictureUrl = fileStorageService.storeFile(userPicture, "user_pictures");
            clientDTO.setUserPictureUrl(userPictureUrl);
        }

        User user = userService.findById(clientDTO.getUserId());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if(!"null".equalsIgnoreCase(user.getUserPictureUrl())) fileStorageService.deleteFile(user.getUserPictureUrl());
        updateUser(user, clientDTO);
        user.setUpdatedAt(LocalDateTime.now());
        user = userService.updateUser(user);

        if ("driver".equalsIgnoreCase(clientDTO.getRole())) {
            Driver driver = driverService.findByUserId(user.getUserId());
            if (driver == null) {


                if (cv != null) {
                    String cvUrl = fileStorageService.storeFile(cv, "cvs");
                    clientDTO.setCvUrl(cvUrl);
                }

                //CREATION DU CONDUCTEUR
                driver = convertToDriver(clientDTO);
                driver.setUserId(user.getUserId());
                driver.setCreatedAt(LocalDateTime.now());
                driver.setUpdatedAt(LocalDateTime.now());

                //conducteur avec vehicule

                if (vehiclePicture != null) {
                        String vehiclePictureUrl = fileStorageService.storeFile(vehiclePicture, "vehicle_pictures");
                        clientDTO.setVehiclePictureUrl(vehiclePictureUrl);
                        Vehicle vehicle=extractVehicle(clientDTO);
                        vehicle=vehicleService.save(vehicle);
                        driver.setVehicleId(vehicle.getVehicleId());
                }
                else driver.setVehicleId("null");//conducteur sans vehicule
                driver=driverService.saveDriver(driver);

            } else {

                if (cv != null) {
                    String cvUrl = fileStorageService.storeFile(cv, "cvs");
                    clientDTO.setCvUrl(cvUrl);

                    //supprimer le cv existant
                    if(!"null".equalsIgnoreCase(driver.getCvUrl())) fileStorageService.deleteFile(driver.getCvUrl());


                }

                if (vehiclePicture != null) {
                    String vehiclePictureUrl = fileStorageService.storeFile(vehiclePicture, "vehicle_pictures");
                    clientDTO.setVehiclePictureUrl(vehiclePictureUrl);
                    //supprimer  l'image d existence
                    if(!"null".equalsIgnoreCase(driver.getVehicleId())) fileStorageService.deleteFile(driver.getVehicleId());

                }
                Vehicle vehicle = vehicleService.findByVehicleId(driver.getVehicleId());
                updateVehicle(vehicle,clientDTO);
                updateDriver(driver, clientDTO);
                driver.setUpdatedAt(LocalDateTime.now());

                vehicleService.updateVehicle(vehicle);
                driverService.updateDriver(driver);
            }

        }

        return ResponseEntity.ok(convertToClientDTO(user));
    }

    @GetMapping("/allUsers")
    public ResponseEntity<List<ClientDTO>>getAllUsers() {
       List<ClientDTO> users = clientService.getAllUsers();
//        Response response = new Response();
//        response.setText("allUsers");
//        response.setData(users);
//        return new ResponseEntity<>(response, HttpStatus.OK);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ClientDTO> getUserById(@PathVariable UUID userId) {
        Optional<User> user = userService.getUserById(userId);
//        Response response = new Response();
//        if(user.isEmpty()){
//            response.setText("user not found in the database");
//            response.setData(null);
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }

//        response.setText("user found in the database");
//        response.setData(convertToClientDTO(user.get()));
//        return new ResponseEntity<>(response, HttpStatus.FOUND);
       return  ResponseEntity.ok(convertToClientDTO(user.get()));
    }

    //LES UTILITAIRES

    private User convertToUser(ClientDTO clientDTO) {
        return User.builder()
                .name(clientDTO.getName())
                .email(clientDTO.getEmail())
                .phoneNumber(clientDTO.getPhoneNumber())
                .address(clientDTO.getAddress())
                .password(clientDTO.getPassword())
                .role(clientDTO.getRole())
                .sex(clientDTO.getSex())
                .birthday(clientDTO.getBirthday())
                .emailNotifications(clientDTO.getEmailNotifications())
                .pushNotifications(clientDTO.getPushNotifications())
                .smsNotifications(clientDTO.getSmsNotifications())
                .userPictureUrl(clientDTO.getUserPictureUrl())
                .build();
    }

    private Driver convertToDriver(ClientDTO clientDTO) {

        return Driver.builder()
                .experiences(clientDTO.getExperiences())
                .driverLicense(clientDTO.getDriverLicense())
                .interests(clientDTO.getInterests())
                .state(clientDTO.getState())
                .profile(clientDTO.getProfile())
                .zones(clientDTO.getZones())
                .cvUrl(clientDTO.getCvUrl())
                .build();
    }


    private void updateUser(User user, ClientDTO clientDTO) {
        user.setName(clientDTO.getName());
        user.setEmail(clientDTO.getEmail());
        user.setPhoneNumber(clientDTO.getPhoneNumber());
        user.setAddress(clientDTO.getAddress());
        user.setPassword(clientDTO.getPassword());
        user.setRole(clientDTO.getRole());
        user.setSex(clientDTO.getSex());
        user.setBirthday(clientDTO.getBirthday());
        user.setEmailNotifications(clientDTO.getEmailNotifications());
        user.setPushNotifications(clientDTO.getPushNotifications());
        user.setSmsNotifications(clientDTO.getSmsNotifications());
        user.setUserPictureUrl(clientDTO.getUserPictureUrl());
    }

    private void updateDriver(Driver driver, ClientDTO clientDTO) {
        driver.setExperiences(clientDTO.getExperiences());
        driver.setDriverLicense(clientDTO.getDriverLicense());
        driver.setInterests(clientDTO.getInterests());
        driver.setState(clientDTO.getState());
        driver.setProfile(clientDTO.getProfile());
        driver.setZones(clientDTO.getZones());
        driver.setCvUrl(clientDTO.getCvUrl());

    }

    private void updateVehicle(Vehicle vehicle,ClientDTO clientDTO) {

        vehicle.setVehiclePictureUrl(clientDTO.getVehiclePictureUrl());
        vehicle.setFueltype(clientDTO.getFueltype());
        vehicle.setBoxtype(clientDTO.getBoxtype());
        vehicle.setPictureDescription(clientDTO.getPictureDescription());
        vehicle.setModel(clientDTO.getModel());
        vehicle.setBrand(clientDTO.getBrand());
    }

    private ClientDTO convertToClientDTO(User user) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setUserId(user.getUserId());
        clientDTO.setName(user.getName());
        clientDTO.setEmail(user.getEmail());
        clientDTO.setPhoneNumber(user.getPhoneNumber());
        clientDTO.setAddress(user.getAddress());
        clientDTO.setPassword(user.getPassword());
        clientDTO.setRole(user.getRole());
        clientDTO.setSex(user.getSex());
        clientDTO.setBirthday(user.getBirthday());
        clientDTO.setCreatedAt(user.getCreatedAt());
        clientDTO.setUpdatedAt(user.getUpdatedAt());
        clientDTO.setEmailNotifications(user.getEmailNotifications());
        clientDTO.setPushNotifications(user.getPushNotifications());
        clientDTO.setSmsNotifications(user.getSmsNotifications());
        clientDTO.setUserPictureUrl(user.getUserPictureUrl());

        if ("driver".equalsIgnoreCase(user.getRole())) {
            Driver driver = driverService.findByUserId(user.getUserId());
            if (driver != null) {
                clientDTO.setDriverId(driver.getDriverId());
                clientDTO.setExperiences(driver.getExperiences());
                clientDTO.setDriverLicense(driver.getDriverLicense());
                clientDTO.setInterests(driver.getInterests());
                clientDTO.setState(driver.getState());
                clientDTO.setProfile(driver.getProfile());
                clientDTO.setZones(driver.getZones());
                clientDTO.setCvUrl(driver.getCvUrl());

                Vehicle vehicle = vehicleService.findByVehicleId(driver.getVehicleId());
                if (vehicle != null) {
                    clientDTO.setVehicleID(vehicle.getVehicleId());
                    clientDTO.setVehiclePictureUrl(vehicle.getVehiclePictureUrl());
                    clientDTO.setPictureDescription(vehicle.getPictureDescription());
                    clientDTO.setBrand(vehicle.getBrand());
                    clientDTO.setBoxtype(vehicle.getBoxtype());
                    clientDTO.setFueltype(vehicle.getBoxtype());
                    clientDTO.setModel(vehicle.getModel());
                }
            }
        }

        return clientDTO;
    }

    private Vehicle extractVehicle(ClientDTO clientDTO) {

        return Vehicle.builder()
                .VehiclePictureUrl(clientDTO.getVehiclePictureUrl())
                .fueltype(clientDTO.getFueltype())
                .boxtype(clientDTO.getBoxtype())
                .PictureDescription(clientDTO.getPictureDescription())
                .model(clientDTO.getModel())
                .brand(clientDTO.getBrand())
                .build();
    }


}

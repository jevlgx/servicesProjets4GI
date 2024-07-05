package com.service.microservice.service;

import java.util.UUID;

//        LOGIQUE DE VERIFICAATIONDE L EXISTENCE DE L UTILISATEUR
//                COMMUNICATION AVEC LE DRIVER-SERVICE
public class DriverServiceClient {
    public boolean userExists(UUID userId) {
        return true;
    }
}


//@FeignClient(name = "driver-service")
//public interface DriverServiceClient {
//    @GetMapping("/api/users/{userId}/exists")
//    boolean userExists(@PathVariable UUID userId);
//}


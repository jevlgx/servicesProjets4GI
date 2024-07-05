package com.service.microservice.controller;

import com.service.microservice.model.User;
import com.service.microservice.service.UserService;
import com.service.microservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, maxAge = 3600)
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/login/client")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> user = userService.authenticateUser(email, password);
        if (user.isPresent()) {
            // Générer un JWT (vous devez implémenter la génération du token)
            String token = JwtUtil.generateToken(user.get().getEmail(), "user");

            Map<String, String > response = new HashMap<>();
            //response.put("token", token);
            response.put("userId", user.get().getUserId().toString());
            System.out.println(user.get().getUserId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }


}


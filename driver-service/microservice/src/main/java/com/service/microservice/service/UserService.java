package com.service.microservice.service;

import com.service.microservice.dto.ClientDTO;
import com.service.microservice.model.Driver;
import com.service.microservice.model.User;
import com.service.microservice.repository.UserRepository;
import com.service.microservice.utils.CassandraIdGenerator;
import com.service.microservice.utils.Extraction;
import com.service.microservice.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final CassandraIdGenerator cassandraIdGenerator;

    private final UserRepository userRepository;

    @Autowired
    public UserService(CassandraIdGenerator cassandraIdGenerator, UserRepository userRepository, DriverService driverService) {
        this.cassandraIdGenerator = cassandraIdGenerator;
        this.userRepository = userRepository;
       
    }

    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && PasswordUtil.checkPassword(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public User saveUser(User user) {
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        user.setUserId(cassandraIdGenerator.getNextId());
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }


    public Optional<User> getUserById(UUID userid) {
        return userRepository.findById(userid);
    }
    public User findById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(User user) {

        userRepository.updateUser(user.getUserId(),user.getName(),user.getEmail(),user.getPhoneNumber(),
                user.getAddress(),user.getPassword(),user.getRole(),user.getSex(),user.getBirthday(),user.getCreatedAt()
                ,user.getUpdatedAt(),user.getEmailNotifications(),user.getPushNotifications(),user.getSmsNotifications(),
                user.getUserPictureUrl());

        return user;
    }
}
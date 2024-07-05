package com.service.microservice.repository;

import com.service.microservice.model.User;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CassandraRepository<User, UUID> {

    @Query("SELECT * FROM users where email=?0")
    Optional<User> findByEmail(String email);

    @Query("UPDATE users SET name = ?1, email = ?2, phoneNumber = ?3, address = ?4, password=?5,role=?6,sex=?7,birthday=?8,createdAt=?9,updatedAt=?10" +
            ",emailNotification=?11, pushNotification=?12, smsNotification=?13 , UserPictureUrl=?14 WHERE userId = ?0")
    void updateUser(UUID userId, String name, String email, String phoneNumber, String address, String password,
                    String role, String sex, LocalDate birthday, LocalDateTime createdAt, LocalDateTime updatedAt,
                    Boolean emailNotification, Boolean pushNotification, Boolean smsNotification,String UserPictureUrl);

}
package com.service.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(value= "drivers")
public class Driver {
    @PrimaryKey
    private UUID driverId;
    private UUID userId;
    private String vehicleId="null";
    private int experiences;
    private String driverLicense;
    private String interests;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String state;
    private String profile;
    private String cvUrl="null";
    @CassandraType(type = CassandraType.Name.SET, typeArguments = {CassandraType.Name.TEXT})
    private Set<String> zones;

}
package com.service.microservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("average_ratings")
public class AverageRating {
    @PrimaryKey
    private UUID driverId;
    private BigDecimal averageRating;
    private int totalReviews;
    private LocalDateTime updatedAt;
}
package com.service.microservice.repository;

import com.service.microservice.model.AverageRating;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface AverageRatingRepository extends CassandraRepository<AverageRating, UUID> {
}

package com.service.microservice.repository;

import com.service.microservice.model.DriverReview;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface DriverReviewRepository extends CassandraRepository<DriverReview, UUID> {
    @AllowFiltering
    List<DriverReview> findByDriverId(UUID driverId);
    @AllowFiltering
    DriverReview findByReservationId(UUID reservationId);

}

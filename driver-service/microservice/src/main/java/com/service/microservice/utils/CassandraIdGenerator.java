package com.service.microservice.utils;


import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



import java.util.UUID;

@Component
public class CassandraIdGenerator {

    private final CqlSession cqlSession;

    @Autowired
    public CassandraIdGenerator(CqlSession cqlSession) {
        this.cqlSession = cqlSession;
    }

    public UUID getNextId() {
        return UUID.randomUUID();



    }
}
package com.service.microservice.dto;

import lombok.Data;

@Data
public class Response {
    private String text;
    private Object data;
    private String value;

}
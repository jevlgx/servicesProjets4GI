package com.projet4gi.exceptions;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus status;

    public ApplicationException(String error, HttpStatus httpStatus){
        super(error);
        this.status = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return this.status;
    }
}

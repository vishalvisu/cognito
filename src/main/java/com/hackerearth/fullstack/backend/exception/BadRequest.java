package com.hackerearth.fullstack.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequest extends Exception{

    private static final long serialVersionUID = 1L;

    public BadRequest(String message){
        super(message);
    }
}

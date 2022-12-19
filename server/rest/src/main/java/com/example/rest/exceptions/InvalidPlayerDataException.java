package com.example.rest.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Player Not Found")
public class InvalidPlayerDataException extends RuntimeException   {
    public InvalidPlayerDataException(String errorMessage) {
        super(errorMessage);
    }
}



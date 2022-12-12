package com.example.rest.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Game Not Found")
public class InvalidGameInstanceException extends Exception  {
    public InvalidGameInstanceException(String errorMessage) {
        super(errorMessage);
    }
}
package com.example.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Actor Not Found")
public class PlayerNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
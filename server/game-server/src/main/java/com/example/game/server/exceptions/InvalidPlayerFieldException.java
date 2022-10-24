package com.example.game.server.exceptions;

import com.example.util.*;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Player ID or Player Name Cannot be null")
public class InvalidPlayerFieldException extends Exception {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(InvalidPlayerFieldException.class);
        logger.info("static initialization block: {}",
                PrintUtils.cyan(String.format("this.class/logger = %s", InvalidPlayerFieldException.class.toString()
                ))
        );
    }
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPlayerFieldException(String errorMessage) {
        super(errorMessage);
        PrintUtils.cyan(errorMessage);
    }

}
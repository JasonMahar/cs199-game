package com.example.server.exceptions;

import com.example.util.*;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Player ID or Player Name Cannot be null")
public class PlayerNotFoundException extends Exception {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(PlayerNotFoundException.class);
        String testString = PrintUtils.cyan(String.format("this.class/logger = %s", PlayerNotFoundException.class.toString()));
        logger.info(testString);
    }
    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException(String errorMessage) {
        super(errorMessage);
        PrintUtils.cyan(errorMessage);
    }

}
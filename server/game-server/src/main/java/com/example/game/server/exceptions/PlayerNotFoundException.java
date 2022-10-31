package com.example.game.server.exceptions;

import com.example.util.*;
import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.*;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(PlayerNotFoundException.class);
        logger.info("static initialization block: {}",
                PrintUtils.cyan(String.format("this.class/logger = %s", PlayerNotFoundException.class.toString()
                ))
        );
    }

    @Serial
    private static final long serialVersionUID = 1L;

    public PlayerNotFoundException() {
        super();
    }

    public PlayerNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PlayerNotFoundException(String errorMessage) {super(errorMessage);}

    public PlayerNotFoundException(final Throwable cause) {
        super(cause);
    }
}
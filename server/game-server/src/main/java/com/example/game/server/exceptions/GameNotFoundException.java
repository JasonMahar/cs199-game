package com.example.game.server.exceptions;


import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class GameNotFoundException extends RuntimeException {

    private static final Logger logger;
    static {
        logger = LoggerFactory.getLogger(GameNotFoundException.class);
        logger.info("this.class/logger = {}", GameNotFoundException.class.toString());
    }
    public GameNotFoundException() {
        super();
    }

    public GameNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public GameNotFoundException(final String message) {
        super(message);
    }

    public GameNotFoundException(final Throwable cause) {
        super(cause);
    }

}
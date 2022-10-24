package com.example.game.server.exceptions;


import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class InvalidGameIdException extends RuntimeException {

    private static final Logger logger;
    static {
        logger = LoggerFactory.getLogger(InvalidGameIdException.class);
        logger.info("this.class/logger = {}", InvalidGameIdException.class.toString());
    }
    public InvalidGameIdException() {
        super();
    }

    public InvalidGameIdException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidGameIdException(final String message) {
        super(message);
    }

    public InvalidGameIdException(final Throwable cause) {
        super(cause);
    }

}
package com.example.game.server.exceptions;

import com.example.game.server.errors.*;
import com.example.util.*;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.*;

@ControllerAdvice
public class GameResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static final Logger logger;
    static {
        logger = LoggerFactory.getLogger(GameResponseEntityExceptionHandler.class);
        logger.info("this.class/logger = {}", GameResponseEntityExceptionHandler.class.toString());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(GameNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(PlayerNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleUserNotFoundException(PlayerNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
}

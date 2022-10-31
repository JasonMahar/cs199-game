package com.example.game.server.exceptions;

import java.time.*;

public record ExceptionResponse(LocalDateTime timestamp,
                                String message,
                                String details) {
    
}

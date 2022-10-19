package com.example.server.exceptions;

import org.hibernate.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {


//    @ExceptionHandler(value
//            = { IllegalArgumentException.class, IllegalStateException.class })
//    protected ResponseEntity<Object> handleConflict(
//            RuntimeException ex, WebRequest request) {
//        String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse,
//                new HttpHeaders(), HttpStatus.CONFLICT, request);
//    }
//
//
//    @ExceptionHandler({ConstraintViolationException.class})
//    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
//        final String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }
//
//
//    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
//        public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
//        logger.error("500 Status Code", ex);
//        final String bodyOfResponse = "This should be application specific";
//        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }
}

package com.obviousai.inventoryms.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.NOT_FOUND.value(), new Date(), ex.getMessage(), "Resource Not Found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Other exception handlers can be added here
}

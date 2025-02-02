package com.obviousai.inventoryms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Annotate with @ResponseStatus to automatically return 404 status
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InsufficientInventoryException extends RuntimeException {
    
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

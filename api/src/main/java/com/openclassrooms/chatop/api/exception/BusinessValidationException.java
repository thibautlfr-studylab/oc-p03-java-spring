package com.openclassrooms.chatop.api.exception;

/**
 * Exception thrown when business validation rules are violated.
 * This exception is mapped to HTTP 400 Bad Request status.
 */
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

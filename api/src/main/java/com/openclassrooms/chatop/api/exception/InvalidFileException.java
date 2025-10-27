package com.openclassrooms.chatop.api.exception;

/**
 * Exception thrown when a file upload fails validation.
 * This exception is mapped to HTTP 400 Bad Request status.
 */
public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }

    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

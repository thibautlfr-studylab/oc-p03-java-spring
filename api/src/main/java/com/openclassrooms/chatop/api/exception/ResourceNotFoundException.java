package com.openclassrooms.chatop.api.exception;

/**
 * Exception thrown when a requested resource is not found.
 * This exception is mapped to HTTP 404 Not Found status.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}

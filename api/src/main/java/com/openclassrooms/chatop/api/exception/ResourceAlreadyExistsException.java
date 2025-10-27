package com.openclassrooms.chatop.api.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * This exception is mapped to HTTP 409 Conflict status.
 */
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}

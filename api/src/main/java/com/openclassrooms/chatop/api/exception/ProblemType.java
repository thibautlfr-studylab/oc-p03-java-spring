package com.openclassrooms.chatop.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;

/**
 * Enumeration of all problem types for RFC 9457 compliant error responses.
 * Each type defines the URI path, human-readable title, and HTTP status code.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457.html">RFC 9457</a>
 */
public enum ProblemType {

    /**
     * Resource not found (HTTP 404)
     */
    RESOURCE_NOT_FOUND(
            "/resource-not-found",
            "Resource Not Found",
            HttpStatus.NOT_FOUND
    ),

    /**
     * Resource already exists - duplicate resource (HTTP 409)
     */
    RESOURCE_ALREADY_EXISTS(
            "/resource-already-exists",
            "Resource Already Exists",
            HttpStatus.CONFLICT
    ),

    /**
     * Invalid file upload (HTTP 400)
     */
    INVALID_FILE(
            "/invalid-file",
            "Invalid File Upload",
            HttpStatus.BAD_REQUEST
    ),

    /**
     * Business validation error (HTTP 400)
     */
    BUSINESS_VALIDATION_ERROR(
            "/business-validation-error",
            "Business Validation Failed",
            HttpStatus.BAD_REQUEST
    ),

    /**
     * Bean validation error with field-level details (HTTP 400)
     */
    VALIDATION_ERROR(
            "/validation-error",
            "Validation Failed",
            HttpStatus.BAD_REQUEST
    ),

    /**
     * Invalid credentials during authentication (HTTP 401)
     */
    INVALID_CREDENTIALS(
            "/invalid-credentials",
            "Authentication Failed",
            HttpStatus.UNAUTHORIZED
    ),

    /**
     * File size exceeds maximum limit (HTTP 413)
     */
    FILE_TOO_LARGE(
            "/file-too-large",
            "File Too Large",
            HttpStatus.PAYLOAD_TOO_LARGE
    ),

    /**
     * Internal server error (HTTP 500)
     */
    INTERNAL_SERVER_ERROR(
            "/internal-server-error",
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    private static final String PROBLEM_BASE_URI = "https://chatop.api/problems";

    private final String path;

    @Getter
    private final String title;

    @Getter
    private final HttpStatus status;

    /**
     * Constructor for ProblemType enum.
     *
     * @param path   URI path segment (e.g., "/resource-not-found")
     * @param title  Human-readable title
     * @param status HTTP status code
     */
    ProblemType(String path, String title, HttpStatus status) {
        this.path = path;
        this.title = title;
        this.status = status;
    }

    /**
     * Gets the complete URI for this problem type.
     *
     * @return URI object with the full problem type URI
     */
    public URI getUri() {
        return URI.create(PROBLEM_BASE_URI + path);
    }

    /**
     * Creates a ProblemDetail instance with this type's properties.
     *
     * @param detail   Human-readable explanation of the problem
     * @param instance URI reference to the specific occurrence (usually request path)
     * @return configured ProblemDetail instance
     */
    public ProblemDetail createProblemDetail(String detail, String instance) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(getUri());
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now().toString());

        if (instance != null) {
            problemDetail.setInstance(URI.create(instance));
        }

        return problemDetail;
    }
}

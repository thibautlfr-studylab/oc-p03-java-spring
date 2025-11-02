package com.openclassrooms.chatop.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application using RFC 9457 ProblemDetail.
 * Catches all exceptions and returns standardized error responses compliant with RFC 9457.
 * Follows Spring Boot best practices and SOLID principles.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9457.html">RFC 9457</a>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Could be "https://chatop.api/problems" in a real-world scenario
    private static final String PROBLEM_BASE_URI = "about:blank";

    /**
     * Creates a ProblemDetail instance with common properties.
     *
     * @param status    HTTP status
     * @param type      Problem type URI
     * @param title     Short, human-readable summary
     * @param detail    Human-readable explanation
     * @param instance  URI reference to specific occurrence
     * @return configured ProblemDetail instance
     */
    private ProblemDetail createProblemDetail(
            HttpStatus status,
            String type,
            String title,
            String detail,
            String instance
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setType(URI.create(PROBLEM_BASE_URI + type));
        problemDetail.setTitle(title);
        problemDetail.setProperty("timestamp", Instant.now().toString());

        if (instance != null) {
            problemDetail.setInstance(URI.create(instance));
        }

        return problemDetail;
    }

    /**
     * Handle ResourceNotFoundException - when a resource is not found.
     * Returns HTTP 404 Not Found with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("Resource not found: {}", ex.getMessage());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.NOT_FOUND,
                "/resource-not-found",
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    /**
     * Handle ResourceAlreadyExistsException - when a resource already exists.
     * Returns HTTP 409 Conflict with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        log.warn("Resource already exists: {}", ex.getMessage());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.CONFLICT,
                "/resource-already-exists",
                "Resource Already Exists",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    /**
     * Handle InvalidFileException - when file upload validation fails.
     * Returns HTTP 400 Bad Request with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ProblemDetail> handleInvalidFileException(
            InvalidFileException ex,
            HttpServletRequest request
    ) {
        log.warn("Invalid file upload: {}", ex.getMessage());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "/invalid-file",
                "Invalid File Upload",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle BusinessValidationException - when business rules are violated.
     * Returns HTTP 400 Bad Request with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<ProblemDetail> handleBusinessValidationException(
            BusinessValidationException ex,
            HttpServletRequest request
    ) {
        log.warn("Business validation failed: {}", ex.getMessage());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "/business-validation-error",
                "Business Validation Failed",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle MethodArgumentNotValidException - when @Valid validation fails.
     * Returns HTTP 400 Bad Request with RFC 9457 ProblemDetail and invalid-params extension.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        log.warn("Validation failed: {}", ex.getMessage());

        // Create invalid-params list according to RFC 9457
        List<Map<String, String>> invalidParams = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    return Map.of(
                            "name", fieldName,
                            "reason", message != null ? message : "Validation failed"
                    );
                })
                .collect(Collectors.toList());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "/validation-error",
                "Validation Failed",
                "Invalid request parameters",
                request.getRequestURI()
        );

        // Add RFC 9457 extension for validation errors
        problemDetail.setProperty("invalid-params", invalidParams);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    /**
     * Handle BadCredentialsException - when authentication fails.
     * Returns HTTP 401 Unauthorized with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {
        log.warn("Authentication failed: Invalid credentials");

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "/invalid-credentials",
                "Authentication Failed",
                "Invalid email or password",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    /**
     * Handle UsernameNotFoundException - when user is not found during authentication.
     * Returns HTTP 401 Unauthorized with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUsernameNotFoundException(
            UsernameNotFoundException ex,
            HttpServletRequest request
    ) {
        log.warn("User not found: {}", ex.getMessage());

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "/invalid-credentials",
                "Authentication Failed",
                "Invalid email or password",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    /**
     * Handle MaxUploadSizeExceededException - when uploaded file exceeds size limit.
     * Returns HTTP 413 Payload Too Large with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ProblemDetail> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request
    ) {
        log.warn("File size exceeds maximum allowed size");

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "/file-too-large",
                "File Too Large",
                "File size exceeds the maximum allowed limit of 10MB",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(problemDetail);
    }

    /**
     * Handle all other unexpected exceptions.
     * Returns HTTP 500 Internal Server Error with RFC 9457 ProblemDetail.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected error occurred", ex);

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "/internal-server-error",
                "Internal Server Error",
                "An internal error occurred. Please try again later.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}

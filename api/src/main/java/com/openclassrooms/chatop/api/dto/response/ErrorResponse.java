package com.openclassrooms.chatop.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response DTO.
 * Used to return consistent error information to the client.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response with detailed information")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Bad Request")
    private String error;

    @Schema(description = "Error message describing what went wrong", example = "La description ne peut pas dépasser 2000 caractères")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/api/rentals")
    private String path;

    @Schema(description = "List of detailed validation errors (if applicable)")
    private List<ValidationError> validationErrors;

    /**
     * Represents a single validation error.
     */
    @Data
    @Builder
    @Schema(description = "Detailed validation error for a specific field")
    public static class ValidationError {
        @Schema(description = "Field name that failed validation", example = "description")
        private String field;

        @Schema(description = "Rejected value", example = "A very long description that exceeds...")
        private Object rejectedValue;

        @Schema(description = "Validation error message", example = "must not exceed 2000 characters")
        private String message;
    }
}

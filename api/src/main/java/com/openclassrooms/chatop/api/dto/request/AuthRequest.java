package com.openclassrooms.chatop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Container class for authentication-related request DTOs.
 */
public class AuthRequest {

    /**
     * DTO for user login requests.
     * Contains the credentials needed for user authentication.
     */
    @Schema(description = "User login request")
    public record LoginRequest(
            @NotBlank(message = "Email is required")
            @Email(message = "Email must be valid")
            @Schema(description = "User's email address", example = "user@example.com")
            String email,

            @NotBlank(message = "Password is required")
            @Schema(description = "User's password", example = "SecurePass123!")
            String password
    ) {}

    /**
     * DTO for user registration requests.
     * Contains all required information to create a new user account.
     */
    @Schema(description = "User registration request")
    public record RegisterRequest(
            @NotBlank(message = "Email is required")
            @Email(message = "Email must be valid")
            @Schema(description = "User's email address", example = "user@example.com")
            String email,

            @NotBlank(message = "Name is required")
            @Schema(description = "User's full name", example = "John Doe")
            String name,

            @NotBlank(message = "Password is required")
            @Schema(description = "User's password", example = "SecurePass123!")
            String password
    ) {}
}

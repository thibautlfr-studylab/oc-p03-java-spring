package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.response.AuthResponse;
import com.openclassrooms.chatop.api.dto.request.AuthRequest.LoginRequest;
import com.openclassrooms.chatop.api.dto.request.AuthRequest.RegisterRequest;
import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.service.interfaces.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication endpoints.
 * Handles user registration, login, and current user information retrieval.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and user registration endpoints")
public class AuthController {

    private final IAuthService authService;

    /**
     * Register a new user.
     * Creates a new user account with encrypted password and returns a JWT token.
     *
     * @param request the registration request with email, name, and password
     * @return AuthResponse with JWT token
     */
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with encrypted password and returns a JWT token. No authentication required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input or email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Return 400 Bad Request if email already exists or validation fails
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    /**
     * Authenticate a user.
     * Validates credentials and returns a JWT token if successful.
     *
     * @param request the login request with email and password
     * @return AuthResponse with JWT token
     */
    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user with email and password, returns a JWT token if successful. No authentication required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return 401 Unauthorized if credentials are invalid
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid credentials"));
        }
    }

    /**
     * Get current authenticated user information.
     * Requires a valid JWT token in the Authorization header.
     *
     * @return UserDTO with current user information
     */
    @GetMapping("/me")
    @Operation(
            summary = "Get current user",
            description = "Returns the authenticated user's information. Requires a valid JWT token in the Authorization header.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User information retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Not authenticated or invalid token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDTO user = authService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Return 401 Unauthorized if user is not authenticated
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Not authenticated"));
        }
    }

    /**
     * Simple error response class.
     */
    private record ErrorResponse(String message) {
    }
}

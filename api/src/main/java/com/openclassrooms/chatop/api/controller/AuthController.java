package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.auth.AuthResponse;
import com.openclassrooms.chatop.api.dto.auth.LoginRequest;
import com.openclassrooms.chatop.api.dto.auth.RegisterRequest;
import com.openclassrooms.chatop.api.dto.user.UserDTO;
import com.openclassrooms.chatop.api.service.AuthService;
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
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new user.
     * Creates a new user account with encrypted password and returns a JWT token.
     *
     * @param request the registration request with email, name, and password
     * @return AuthResponse with JWT token
     */
    @PostMapping("/register")
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

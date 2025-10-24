package com.openclassrooms.chatop.api.service.interfaces;

import com.openclassrooms.chatop.api.dto.auth.AuthResponse;
import com.openclassrooms.chatop.api.dto.auth.LoginRequest;
import com.openclassrooms.chatop.api.dto.auth.RegisterRequest;
import com.openclassrooms.chatop.api.dto.user.UserDTO;

/**
 * Service interface for authentication operations.
 * Defines the contract for user registration, login, and user information retrieval.
 */
public interface IAuthService {

    /**
     * Register a new user.
     * Encrypts the password and creates a new user account.
     *
     * @param request the registration request containing user details
     * @return AuthResponse with JWT token
     * @throws RuntimeException if email already exists
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authenticate a user and generate JWT token.
     *
     * @param request the login request containing credentials
     * @return AuthResponse with JWT token
     * @throws RuntimeException if authentication fails
     */
    AuthResponse login(LoginRequest request);

    /**
     * Get current authenticated user information.
     *
     * @return UserDTO with user information
     * @throws RuntimeException if user is not authenticated
     */
    UserDTO getCurrentUser();

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return UserDTO with user information
     * @throws RuntimeException if user not found
     */
    UserDTO getUserById(Long id);
}

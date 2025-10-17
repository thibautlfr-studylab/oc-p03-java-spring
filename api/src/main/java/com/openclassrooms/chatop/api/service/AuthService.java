package com.openclassrooms.chatop.api.service;

import com.openclassrooms.chatop.api.dto.AuthResponse;
import com.openclassrooms.chatop.api.dto.LoginRequest;
import com.openclassrooms.chatop.api.dto.RegisterRequest;
import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service handling authentication business logic.
 * Manages user registration, login, and user information retrieval.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user.
     * Encrypts the password and creates a new user account.
     *
     * @param request the registration request containing user details
     * @return AuthResponse with JWT token
     * @throws RuntimeException if email already exists
     */
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Save user to database
        userRepository.save(user);

        // Generate JWT token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    /**
     * Authenticate a user and generate JWT token.
     *
     * @param request the login request containing credentials
     * @return AuthResponse with JWT token
     * @throws RuntimeException if authentication fails
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Load user details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }

    /**
     * Get current authenticated user information.
     *
     * @return UserDTO with user information
     * @throws RuntimeException if user is not authenticated
     */
    public UserDTO getCurrentUser() {
        // Get the currently authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName();

        // Find user in database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Convert to DTO
        return convertToDTO(user);
    }

    /**
     * Get user by ID.
     *
     * @param id the user ID
     * @return UserDTO with user information
     * @throws RuntimeException if user not found
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return convertToDTO(user);
    }

    /**
     * Convert User entity to UserDTO.
     *
     * @param user the User entity
     * @return UserDTO
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreated_at(user.getCreatedAt());
        dto.setUpdated_at(user.getUpdatedAt());
        return dto;
    }
}

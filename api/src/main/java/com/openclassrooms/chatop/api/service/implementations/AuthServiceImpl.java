package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.dto.request.AuthRequest.LoginRequest;
import com.openclassrooms.chatop.api.dto.request.AuthRequest.RegisterRequest;
import com.openclassrooms.chatop.api.dto.response.AuthResponse;
import com.openclassrooms.chatop.api.exception.ResourceAlreadyExistsException;
import com.openclassrooms.chatop.api.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.api.mapper.UserMapper;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.interfaces.IAuthService;
import com.openclassrooms.chatop.api.service.interfaces.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation handling authentication business logic.
 * Manages user registration, login, and user information retrieval.
 * Uses MapStruct's UserMapper for entity-DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("User", "email", request.email());
        }

        // Create a new user
        User user = UserMapper.INSTANCE.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));


        // Save user to the database
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

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Load user details
        User user = userRepository.findByEmail(request.email())
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

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        // Get the currently authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }

        String email = authentication.getName();

        // Find user in the database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Convert to DTO using UserMapper
        return UserMapper.INSTANCE.toDto(user);
    }
}

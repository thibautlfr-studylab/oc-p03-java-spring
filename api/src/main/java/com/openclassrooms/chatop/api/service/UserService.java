package com.openclassrooms.chatop.api.service;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service layer for user operations.
 * Handles business logic for user-related functionality.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieve a user by their ID.
     *
     * @param id the user ID to search for
     * @return Optional containing the UserDTO if found, empty otherwise
     */
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserDTO::fromEntity);
    }
}

package com.openclassrooms.chatop.api.service.interfaces;

import com.openclassrooms.chatop.api.dto.user.UserDTO;

import java.util.Optional;

/**
 * Service interface for user operations.
 * Defines the contract for user-related functionality.
 */
public interface IUserService {

    /**
     * Retrieve a user by their ID.
     *
     * @param id the user ID to search for
     * @return Optional containing the UserDTO if found, empty otherwise
     */
    Optional<UserDTO> getUserById(Long id);
}

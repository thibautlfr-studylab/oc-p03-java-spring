package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.mapper.UserMapper;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for user operations.
 * Handles business logic for user-related functionality.
 * Uses MapStruct's UserMapper for entity-DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper.INSTANCE::toDto);
    }
}

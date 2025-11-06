package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.MessageDTO;
import com.openclassrooms.chatop.api.dto.request.MessageRequest.CreateMessageRequest;
import com.openclassrooms.chatop.api.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.api.mapper.MessageMapper;
import com.openclassrooms.chatop.api.mapper.UserMapper;
import com.openclassrooms.chatop.api.model.Message;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.MessageRepository;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.interfaces.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for message operations.
 * Handles business logic for message-related functionality.
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final MessageMapper messageMapper;
    private final AuthServiceImpl authService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public MessageDTO createMessage(CreateMessageRequest request) {
        // Get current authenticated user
        User user = userMapper.toEntity(authService.getCurrentUser());

        // Validate rental exists
        Rental rental = rentalRepository.findById(request.rental_id())
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", request.rental_id()));

        // Create message entity
        Message message = messageMapper.toEntity(request);
        message.setUser(user);
        message.setRental(rental);

        // Save message
        return messageMapper.toDto(messageRepository.save(message));
    }
}

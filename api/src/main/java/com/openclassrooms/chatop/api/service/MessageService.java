package com.openclassrooms.chatop.api.service;

import com.openclassrooms.chatop.api.dto.message.CreateMessageRequest;
import com.openclassrooms.chatop.api.model.Message;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.MessageRepository;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import com.openclassrooms.chatop.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for message operations.
 * Handles business logic for message-related functionality.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    /**
     * Create a new message.
     *
     * @param request the message data containing user_id, rental_id, and message text
     * @throws IllegalArgumentException if user or rental doesn't exist
     */
    @Transactional
    public void createMessage(CreateMessageRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.getUser_id()));

        // Validate rental exists
        Rental rental = rentalRepository.findById(request.getRental_id())
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + request.getRental_id()));

        // Create message entity
        Message message = new Message();
        message.setMessage(request.getMessage());
        message.setUser(user);
        message.setRental(rental);

        // Save message
        messageRepository.save(message);
    }
}

package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.request.MessageRequest.CreateMessageRequest;
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

    @Override
    @Transactional
    public void createMessage(CreateMessageRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.user_id())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + request.user_id()));

        // Validate rental exists
        Rental rental = rentalRepository.findById(request.rental_id())
                .orElseThrow(() -> new IllegalArgumentException("Rental not found with id: " + request.rental_id()));

        // Create message entity
        Message message = new Message();
        message.setMessage(request.message());
        message.setUser(user);
        message.setRental(rental);

        // Save message
        messageRepository.save(message);
    }
}

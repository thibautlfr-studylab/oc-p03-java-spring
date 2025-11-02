package com.openclassrooms.chatop.api.service.interfaces;

import com.openclassrooms.chatop.api.dto.MessageDTO;
import com.openclassrooms.chatop.api.dto.request.MessageRequest.CreateMessageRequest;

/**
 * Service interface for message operations.
 * Defines the contract for message-related functionality.
 */
public interface IMessageService {

    /**
     * Create a new message.
     *
     * @param request the message data containing user_id, rental_id, and message text
     * @throws IllegalArgumentException if user or rental doesn't exist
     */
    MessageDTO createMessage(CreateMessageRequest request);
}

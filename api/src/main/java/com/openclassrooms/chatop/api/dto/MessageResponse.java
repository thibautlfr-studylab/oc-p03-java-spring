package com.openclassrooms.chatop.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic DTO for simple message responses.
 * Used for success messages like "Rental created !" or "Rental updated !".
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private String message;
}

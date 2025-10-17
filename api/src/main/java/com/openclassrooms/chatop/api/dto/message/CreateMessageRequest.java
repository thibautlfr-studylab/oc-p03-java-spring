package com.openclassrooms.chatop.api.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new message.
 * Used to receive data from POST /api/messages requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageRequest {

    @NotNull(message = "Rental ID is required")
    private Long rental_id;

    @NotNull(message = "User ID is required")
    private Long user_id;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    private String message;
}

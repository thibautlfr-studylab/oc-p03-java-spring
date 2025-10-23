package com.openclassrooms.chatop.api.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request to create a new message")
public class CreateMessageRequest {

    @NotNull(message = "Rental ID is required")
    @Schema(description = "ID of the rental property", example = "1")
    private Long rental_id;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user sending the message", example = "1")
    private Long user_id;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message must not exceed 2000 characters")
    @Schema(description = "Message content", example = "I'm interested in this property. Is it still available?")
    private String message;
}

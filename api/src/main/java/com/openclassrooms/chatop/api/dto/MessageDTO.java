package com.openclassrooms.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for message information responses.
 * Used to expose message data in API responses.
 */
@Schema(description = "Message information")
public record MessageDTO(
        @Schema(description = "Message's unique identifier", example = "1")
        Long id,

        @Schema(description = "Message content", example = "I am interested in this rental property")
        String message,

        @Schema(description = "Rental property ID associated with this message", example = "5")
        Long rental_id,

        @Schema(description = "User ID who sent the message", example = "2")
        Long user_id,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Message creation date", example = "2024/01/15")
        LocalDateTime created_at,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Last message update date", example = "2024/01/15")
        LocalDateTime updated_at
) {
}

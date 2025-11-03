package com.openclassrooms.chatop.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic DTO for simple success responses.
 * Used for success messages like "Rental created !" or "Rental updated !".
 */
@Schema(description = "Generic success message response with optional data")
public record SuccessResponse(
        @Schema(description = "Success or status message", example = "Rental created !")
        String message,

        @Schema(description = "Optional data associated with the response", nullable = true)
        Object data
) {
        public SuccessResponse(String message) {
                this(message, null); // constructeur pratique si tu n’as pas de data
        }
}

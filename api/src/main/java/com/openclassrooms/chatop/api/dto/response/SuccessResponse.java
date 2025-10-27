package com.openclassrooms.chatop.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Generic DTO for simple success responses.
 * Used for success messages like "Rental created !" or "Rental updated !".
 */
@Schema(description = "Generic success message response")
public record SuccessResponse(
        @Schema(description = "Success or status message", example = "Rental created !")
        String message
) {
}

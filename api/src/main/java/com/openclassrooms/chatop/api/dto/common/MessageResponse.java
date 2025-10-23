package com.openclassrooms.chatop.api.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Generic success message response")
public class MessageResponse {

    @Schema(description = "Success or status message", example = "Rental created !")
    private String message;
}

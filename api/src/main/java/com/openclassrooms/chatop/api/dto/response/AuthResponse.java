package com.openclassrooms.chatop.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for authentication responses.
 * Returns the JWT token after successful login or registration.
 */
@Schema(description = "Authentication response containing JWT token")
public record AuthResponse(
        @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}

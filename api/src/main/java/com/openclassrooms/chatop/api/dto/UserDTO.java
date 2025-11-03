package com.openclassrooms.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for user information responses.
 * Used to expose user data without revealing sensitive information like passwords.
 * Mapping between User entity and UserDTO is handled by MapStruct via UserMapper.
 */
@Schema(description = "User information")
public record UserDTO(
        @Schema(description = "User's unique identifier", example = "1")
        Long id,

        @Schema(description = "User's full name", example = "John Doe")
        String name,

        @Schema(description = "User's email address", example = "user@example.com")
        String email,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Account creation date", example = "2024/01/15")
        LocalDateTime created_at,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Last account update date", example = "2024/01/15")
        LocalDateTime updated_at
) {
}

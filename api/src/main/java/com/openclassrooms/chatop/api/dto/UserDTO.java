package com.openclassrooms.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.api.model.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO for user information responses.
 * Used to expose user data without revealing sensitive information like passwords.
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
    /**
     * Convert a User entity to a UserDTO.
     * This method ensures that sensitive information (like password) is never exposed.
     *
     * @param user the User entity to convert
     * @return a UserDTO containing the user's public information
     */
    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}

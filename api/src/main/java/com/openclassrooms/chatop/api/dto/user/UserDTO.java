package com.openclassrooms.chatop.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for user information responses.
 * Used to expose user data without revealing sensitive information like passwords.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updated_at;

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

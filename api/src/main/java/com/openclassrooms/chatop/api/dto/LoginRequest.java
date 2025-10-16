package com.openclassrooms.chatop.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login requests.
 * Contains the credentials needed for user authentication.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Login is required")
    @Email(message = "Login must be valid")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;
}

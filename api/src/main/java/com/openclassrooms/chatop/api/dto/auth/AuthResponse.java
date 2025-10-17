package com.openclassrooms.chatop.api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication responses.
 * Returns the JWT token after successful login or registration.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
}

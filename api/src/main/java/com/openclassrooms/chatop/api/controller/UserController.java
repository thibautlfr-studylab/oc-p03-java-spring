package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.user.UserDTO;
import com.openclassrooms.chatop.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for user endpoints.
 * Handles HTTP requests for user-related operations.
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;

    /**
     * Get user information by ID.
     * Returns user details if found, or 404 if user doesn't exist.
     *
     * @param id the user ID to retrieve
     * @return ResponseEntity with UserDTO if found, 404 otherwise
     */
    @GetMapping("/user/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information for the specified ID. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<UserDTO> getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

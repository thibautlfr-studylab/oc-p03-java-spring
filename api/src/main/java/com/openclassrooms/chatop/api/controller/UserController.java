package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.api.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user endpoints.
 * Handles HTTP requests for user-related operations.
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final IUserService userService;

    /**
     * Get user information by ID.
     * Returns user details if found, or throws ResourceNotFoundException.
     *
     * @param id the user ID to retrieve
     * @return UserDTO with user information
     * @throws ResourceNotFoundException if user is not found
     */
    @GetMapping("/user/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information for the specified ID. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public UserDTO getUserById(
            @Parameter(description = "User ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        return userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
}

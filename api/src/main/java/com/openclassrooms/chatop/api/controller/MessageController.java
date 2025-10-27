package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.request.MessageRequest.CreateMessageRequest;
import com.openclassrooms.chatop.api.dto.response.SuccessResponse;
import com.openclassrooms.chatop.api.service.interfaces.IMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for message-related endpoints.
 * Handles HTTP requests for message operations.
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Message management API")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final IMessageService messageService;

    /**
     * Create a new message.
     * POST /api/messages
     *
     * @param request the message data
     * @return success message
     */
    @PostMapping
    @Operation(summary = "Send a message", description = "Send a message to a rental property owner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request - missing required fields",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - authentication required",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User or rental not found",
                    content = @Content)
    })
    public ResponseEntity<SuccessResponse> createMessage(@Valid @RequestBody CreateMessageRequest request) {
        messageService.createMessage(request);
        return ResponseEntity.ok(new SuccessResponse("Message send with success"));
    }
}

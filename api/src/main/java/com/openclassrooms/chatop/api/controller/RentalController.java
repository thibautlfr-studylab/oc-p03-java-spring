package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.dto.response.ErrorResponse;
import com.openclassrooms.chatop.api.dto.response.RentalListResponse;
import com.openclassrooms.chatop.api.dto.response.SuccessResponse;
import com.openclassrooms.chatop.api.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.interfaces.IRentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for rental endpoints.
 * Handles HTTP requests for rental-related operations.
 */
@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Rental management endpoints")
public class RentalController {

    private final IRentalService rentalService;
    private final UserRepository userRepository;

    /**
     * Get all rentals.
     * Returns a list of all rental properties.
     *
     * @return ResponseEntity with RentalListResponse containing all rentals
     */
    @GetMapping
    @Operation(
            summary = "Get all rentals",
            description = "Returns a list of all rental properties. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rentals retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalListResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content
            )
    })
    public ResponseEntity<RentalListResponse> getAllRentals() {
        List<RentalDTO> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(new RentalListResponse(rentals));
    }

    /**
     * Get rental by ID.
     * Returns details of a specific rental property.
     *
     * @param id the rental ID to retrieve
     * @return ResponseEntity with RentalDTO if found, 404 otherwise
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get rental by ID",
            description = "Returns details of a specific rental property. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<RentalDTO> getRentalById(
            @Parameter(description = "Rental ID", required = true, example = "1")
            @PathVariable Long id
    ) {
        RentalDTO rental = rentalService.getRentalById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));
        return ResponseEntity.ok(rental);
    }

    /**
     * Create a new rental.
     * Creates a new rental property with an image.
     *
     * @param name rental name
     * @param surface rental surface in square meters
     * @param price rental price per night
     * @param description rental description
     * @param picture rental picture file
     * @param userDetails authenticated user
     * @return ResponseEntity with success message
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental property with an image upload. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Invalid input data, missing fields, or invalid file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<SuccessResponse> createRental(
            @Parameter(description = "Rental name", required = true)
            @RequestParam("name") String name,

            @Parameter(description = "Rental surface in square meters", required = true)
            @RequestParam("surface") BigDecimal surface,

            @Parameter(description = "Rental price per night", required = true)
            @RequestParam("price") BigDecimal price,

            @Parameter(description = "Rental description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Rental picture file", required = true)
            @RequestParam("picture") MultipartFile picture,

            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Create request DTO
        CreateRentalRequest request = new CreateRentalRequest(name, surface, price, description);

        // Get the authenticated user from UserRepository
        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userDetails.getUsername()));

        // Create rental (validation happens in the service layer)
        RentalDTO createdRental = rentalService.createRental(request, picture, owner);

        return ResponseEntity.ok(new SuccessResponse("Rental created !"));
    }

    /**
     * Update an existing rental.
     * Updates rental details, optionally including a new image.
     *
     * @param id rental ID to update
     * @param name new rental name
     * @param surface new rental surface
     * @param price new rental price
     * @param description new rental description
     * @param picture optional new rental picture
     * @return ResponseEntity with success message if updated, 404 if not found
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update a rental",
            description = "Updates an existing rental property. All fields are optional. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Invalid input data or invalid file",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public ResponseEntity<SuccessResponse> updateRental(
            @Parameter(description = "Rental ID", required = true, example = "1")
            @PathVariable Long id,

            @Parameter(description = "New rental name")
            @RequestParam(value = "name", required = false) String name,

            @Parameter(description = "New rental surface in square meters")
            @RequestParam(value = "surface", required = false) BigDecimal surface,

            @Parameter(description = "New rental price per night")
            @RequestParam(value = "price", required = false) BigDecimal price,

            @Parameter(description = "New rental description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "New rental picture file")
            @RequestParam(value = "picture", required = false) MultipartFile picture
    ) {
        // Create request DTO
        UpdateRentalRequest request = new UpdateRentalRequest(name, surface, price, description);

        // Update rental (validation happens in service layer)
        rentalService.updateRental(id, request, picture)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));

        return ResponseEntity.ok(new SuccessResponse("Rental updated !"));
    }
}

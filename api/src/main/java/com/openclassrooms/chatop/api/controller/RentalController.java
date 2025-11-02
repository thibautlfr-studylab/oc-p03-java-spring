package com.openclassrooms.chatop.api.controller;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.dto.response.RentalListResponse;
import com.openclassrooms.chatop.api.dto.response.SuccessResponse;
import com.openclassrooms.chatop.api.exception.ResourceNotFoundException;
import com.openclassrooms.chatop.api.service.interfaces.IRentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Get all rentals.
     * Returns a list of all rental properties.
     *
     * @return ResponseEntity with RentalListResponse containing all rentals
     */
    @GetMapping
    @Operation(
            summary = "Get all rentals",
            description = "Returns a list of all rental properties. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rentals retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RentalListResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
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
            description = "Returns details of a specific rental property. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental found successfully",
                    content = @Content(schema = @Schema(implementation = RentalDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
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
     * Creates a new rental property with an image.
     *
     * @param request     rental data
     * @param userDetails authenticated user details
     * @return ResponseEntity with success message and created rental data
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental property with an image upload. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rental created successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Invalid input data, missing fields, or invalid file",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<SuccessResponse> createRental(
            @Valid @ModelAttribute CreateRentalRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        RentalDTO rentalDto = rentalService.createRental(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SuccessResponse("Rental created !", rentalDto));
    }

    /**
     * Update an existing rental.
     * Updates rental details, optionally including a new image.
     *
     * @param id      rental ID to update
     * @param request updated rental data
     * @return ResponseEntity with success message if updated, 404 if not found
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update a rental",
            description = "Updates an existing rental property. All fields are optional. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental updated successfully",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request - Invalid input data or invalid file",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - Invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Rental not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    public ResponseEntity<SuccessResponse> updateRental(
            @Parameter(description = "Rental ID", required = true, example = "1")
            @PathVariable Long id,

            @Valid @ModelAttribute UpdateRentalRequest request
    ) {
        RentalDTO rentalDTO = rentalService.updateRental(id, request)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", "id", id));
        return ResponseEntity.ok(new SuccessResponse("Rental updated !", rentalDTO));
    }
}

package com.openclassrooms.chatop.api.dto.response;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * DTO for wrapping a list of rentals.
 * Used for GET /api/rentals endpoint to match frontend expectations.
 */
@Schema(description = "Response containing a list of rental properties")
public record RentalListResponse(
        @Schema(description = "List of rental properties")
        List<RentalDTO> rentals
) {
}

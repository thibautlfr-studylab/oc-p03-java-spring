package com.openclassrooms.chatop.api.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for wrapping a list of rentals.
 * Used for GET /api/rentals endpoint to match frontend expectations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing a list of rental properties")
public class RentalListResponse {

    @Schema(description = "List of rental properties")
    private List<RentalDTO> rentals;
}

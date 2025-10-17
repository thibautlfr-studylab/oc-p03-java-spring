package com.openclassrooms.chatop.api.dto.rental;

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
public class RentalListResponse {
    private List<RentalDTO> rentals;
}

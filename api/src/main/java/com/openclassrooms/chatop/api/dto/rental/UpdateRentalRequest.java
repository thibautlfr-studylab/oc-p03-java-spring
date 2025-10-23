package com.openclassrooms.chatop.api.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for updating an existing rental.
 * All fields are optional to allow partial updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing rental property (all fields optional)")
public class UpdateRentalRequest {

    @Schema(description = "Property name", example = "Updated apartment name")
    private String name;

    @Positive(message = "Surface must be positive")
    @Schema(description = "Property surface area in square meters", example = "70.0")
    private BigDecimal surface;

    @Positive(message = "Price must be positive")
    @Schema(description = "Rental price per night", example = "175.00")
    private BigDecimal price;

    @Schema(description = "Property description", example = "Recently renovated apartment with new amenities")
    private String description;
}

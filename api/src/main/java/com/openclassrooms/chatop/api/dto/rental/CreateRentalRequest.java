package com.openclassrooms.chatop.api.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for creating a new rental.
 * Used to receive data from POST requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new rental property")
public class CreateRentalRequest {

    @NotBlank(message = "Name is required")
    @Schema(description = "Property name", example = "Charming seaside apartment")
    private String name;

    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be positive")
    @Schema(description = "Property surface area in square meters", example = "65.5")
    private BigDecimal surface;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Schema(description = "Rental price per night", example = "150.00")
    private BigDecimal price;

    @Schema(description = "Property description", example = "Beautiful apartment with ocean view, 2 bedrooms, fully equipped kitchen")
    private String description;
}

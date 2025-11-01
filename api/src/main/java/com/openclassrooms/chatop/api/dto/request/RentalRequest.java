package com.openclassrooms.chatop.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * Container class for rental-related request DTOs.
 */
public class RentalRequest {

    /**
     * DTO for creating a new rental.
     * Used to receive data from POST requests.
     */
    @Schema(description = "Request to create a new rental property")
    public record CreateRentalRequest(
            @NotBlank(message = "Name is required")
            @Size(max = 255, message = "Name must not exceed 255 characters")
            @Schema(description = "Property name", example = "Charming seaside apartment")
            String name,

            @NotNull(message = "Surface is required")
            @Positive(message = "Surface must be positive")
            @Schema(description = "Property surface area in square meters", example = "65.50")
            BigDecimal surface,

            @NotNull(message = "Price is required")
            @Positive(message = "Price must be positive")
            @Schema(description = "Rental price per night", example = "150.00")
            BigDecimal price,

            @NotNull(message = "Picture is required")
            @Schema(description = "Property picture file")
            MultipartFile picture,

            @Size(max = 2000, message = "Description must not exceed 2000 characters")
            @Schema(description = "Property description", example = "Beautiful apartment with ocean view, 2 bedrooms, fully equipped kitchen")
            String description
    ) {
    }

    /**
     * DTO for updating an existing rental.
     * All fields are optional to allow partial updates.
     */
    @Schema(description = "Request to update an existing rental property (all fields optional)")
    public record UpdateRentalRequest(
            @Size(max = 255, message = "Name must not exceed 255 characters")
            @Schema(description = "Property name", example = "Updated apartment name")
            String name,

            @Positive(message = "Surface must be positive")
            @Schema(description = "Property surface area in square meters", example = "70.00")
            BigDecimal surface,

            @Positive(message = "Price must be positive")
            @Schema(description = "Rental price per night", example = "175.00")
            BigDecimal price,

            @Schema(description = "New rental picture file")
            MultipartFile picture,

            @Size(max = 2000, message = "Description must not exceed 2000 characters")
            @Schema(description = "Property description", example = "Recently renovated apartment with new amenities")
            String description
    ) {
    }
}

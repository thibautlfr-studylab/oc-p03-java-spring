package com.openclassrooms.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.api.model.Rental;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for rental information responses.
 * Used to expose rental data in API responses.
 */
@Schema(description = "Rental property information")
public record RentalDTO(
        @Schema(description = "Rental property unique identifier", example = "1")
        Long id,

        @Schema(description = "Property name", example = "Charming seaside apartment")
        String name,

        @Schema(description = "Property surface area in square meters", example = "65.5")
        BigDecimal surface,

        @Schema(description = "Rental price per night", example = "150.00")
        BigDecimal price,

        @Schema(description = "URL to the property picture", example = "http://localhost:3001/uploads/property.jpg")
        String picture,

        @Schema(description = "Property description", example = "Beautiful apartment with ocean view, 2 bedrooms, fully equipped kitchen")
        String description,

        @Schema(description = "ID of the property owner", example = "1")
        Long owner_id,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Property creation date", example = "2024/01/15")
        LocalDateTime created_at,

        @JsonFormat(pattern = "yyyy/MM/dd")
        @Schema(description = "Last property update date", example = "2024/01/15")
        LocalDateTime updated_at
) {
    /**
     * Convert a Rental entity to a RentalDTO.
     *
     * @param rental the Rental entity to convert
     * @return a RentalDTO containing the rental's information
     */
    public static RentalDTO fromEntity(Rental rental) {
        if (rental == null) {
            return null;
        }
        return new RentalDTO(
                rental.getId(),
                rental.getName(),
                rental.getSurface(),
                rental.getPrice(),
                rental.getPicture(),
                rental.getDescription(),
                rental.getOwner().getId(),
                rental.getCreatedAt(),
                rental.getUpdatedAt()
        );
    }
}

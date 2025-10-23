package com.openclassrooms.chatop.api.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.api.model.Rental;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for rental information responses.
 * Used to expose rental data in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rental property information")
public class RentalDTO {

    @Schema(description = "Rental property unique identifier", example = "1")
    private Long id;

    @Schema(description = "Property name", example = "Charming seaside apartment")
    private String name;

    @Schema(description = "Property surface area in square meters", example = "65.5")
    private BigDecimal surface;

    @Schema(description = "Rental price per night", example = "150.00")
    private BigDecimal price;

    @Schema(description = "URL to the property picture", example = "http://localhost:3001/uploads/property.jpg")
    private String picture;

    @Schema(description = "Property description", example = "Beautiful apartment with ocean view, 2 bedrooms, fully equipped kitchen")
    private String description;

    @Schema(description = "ID of the property owner", example = "1")
    private Long owner_id;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Property creation date", example = "2024/01/15")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @Schema(description = "Last property update date", example = "2024/01/15")
    private LocalDateTime updated_at;

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

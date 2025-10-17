package com.openclassrooms.chatop.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.openclassrooms.chatop.api.model.Rental;
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
public class RentalDTO {

    private Long id;
    private String name;
    private BigDecimal surface;
    private BigDecimal price;
    private String picture;
    private String description;
    private Long owner_id;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
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

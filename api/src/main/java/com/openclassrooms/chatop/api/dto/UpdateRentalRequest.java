package com.openclassrooms.chatop.api.dto;

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
public class UpdateRentalRequest {

    private String name;

    @Positive(message = "Surface must be positive")
    private BigDecimal surface;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String description;
}

package com.openclassrooms.chatop.api.dto.rental;

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
public class CreateRentalRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Surface is required")
    @Positive(message = "Surface must be positive")
    private BigDecimal surface;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private String description;
}

package com.openclassrooms.chatop.api.service.interfaces;

import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for rental operations.
 * Defines the contract for rental-related functionality.
 */
public interface IRentalService {

    /**
     * Get all rentals.
     *
     * @return List of all rentals as DTOs
     */
    List<RentalDTO> getAllRentals();

    /**
     * Get a rental by ID.
     *
     * @param id the rental ID
     * @return Optional containing the RentalDTO if found, empty otherwise
     */
    Optional<RentalDTO> getRentalById(Long id);

    /**
     * Create a new rental with an image.
     *
     * @param request the rental data
     * @param picture the rental picture file
     * @param owner the user creating the rental
     * @return the created rental as DTO
     */
    RentalDTO createRental(CreateRentalRequest request, MultipartFile picture, User owner);

    /**
     * Update an existing rental.
     *
     * @param id the rental ID to update
     * @param request the updated rental data
     * @param picture optional new picture file (can be null)
     * @return Optional containing the updated rental DTO if found, empty otherwise
     */
    Optional<RentalDTO> updateRental(Long id, UpdateRentalRequest request, MultipartFile picture);
}

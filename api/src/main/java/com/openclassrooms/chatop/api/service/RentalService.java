package com.openclassrooms.chatop.api.service;

import com.openclassrooms.chatop.api.dto.rental.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.rental.RentalDTO;
import com.openclassrooms.chatop.api.dto.rental.UpdateRentalRequest;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for rental operations.
 * Handles business logic for rental-related functionality.
 */
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final FileStorageService fileStorageService;

    /**
     * Get all rentals.
     *
     * @return List of all rentals as DTOs
     */
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get a rental by ID.
     *
     * @param id the rental ID
     * @return Optional containing the RentalDTO if found, empty otherwise
     */
    public Optional<RentalDTO> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(RentalDTO::fromEntity);
    }

    /**
     * Create a new rental with an image.
     *
     * @param request the rental data
     * @param picture the rental picture file
     * @param owner the user creating the rental
     * @return the created rental as DTO
     */
    @Transactional
    public RentalDTO createRental(CreateRentalRequest request, MultipartFile picture, User owner) {
        // Store the picture and get its URL
        String pictureUrl = fileStorageService.storeFile(picture);

        // Create rental entity
        Rental rental = new Rental();
        rental.setName(request.getName());
        rental.setSurface(request.getSurface());
        rental.setPrice(request.getPrice());
        rental.setPicture(pictureUrl);
        rental.setDescription(request.getDescription());
        rental.setOwner(owner);

        // Save and return
        Rental savedRental = rentalRepository.save(rental);
        return RentalDTO.fromEntity(savedRental);
    }

    /**
     * Update an existing rental.
     *
     * @param id the rental ID to update
     * @param request the updated rental data
     * @param picture optional new picture file
     * @return Optional containing the updated rental DTO if found, empty otherwise
     */
    @Transactional
    public Optional<RentalDTO> updateRental(Long id, UpdateRentalRequest request, Optional<MultipartFile> picture) {
        return rentalRepository.findById(id).map(rental -> {
            // Update fields if provided
            if (request.getName() != null && !request.getName().isEmpty()) {
                rental.setName(request.getName());
            }
            if (request.getSurface() != null) {
                rental.setSurface(request.getSurface());
            }
            if (request.getPrice() != null) {
                rental.setPrice(request.getPrice());
            }
            if (request.getDescription() != null) {
                rental.setDescription(request.getDescription());
            }

            // Update picture if provided
            picture.ifPresent(file -> {
                if (!file.isEmpty()) {
                    String pictureUrl = fileStorageService.storeFile(file);
                    rental.setPicture(pictureUrl);
                }
            });

            // Save and return
            Rental updatedRental = rentalRepository.save(rental);
            return RentalDTO.fromEntity(updatedRental);
        });
    }
}

package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.mapper.RentalMapper;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import com.openclassrooms.chatop.api.repository.UserRepository;
import com.openclassrooms.chatop.api.service.interfaces.IFileStorageService;
import com.openclassrooms.chatop.api.service.interfaces.IRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for rental operations.
 * Handles business logic for rental-related functionality.
 * Uses MapStruct's RentalMapper for entity-DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final IFileStorageService fileStorageService;
    private final RentalMapper rentalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RentalDTO> getAllRentals() {
        return rentalMapper.toDtoList(rentalRepository.findAllWithOwner());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RentalDTO> getRentalById(Long id) {
        return rentalRepository.findByIdWithOwner(id).map(rentalMapper::toDto);
    }

    @Override
    @Transactional
    public RentalDTO createRental(CreateRentalRequest request, UserDetails userDetails) {
        // Get the owner (user authenticated) from the UserDetails
        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Store the picture and get its URL (validation happens in FileStorageService)
        String pictureUrl = fileStorageService.storeFile(request.picture());

        // Map the request to a Rental entity and set values not handled by MapStruct
        Rental rental = rentalMapper.toEntity(request);
        rental.setOwner(owner);
        rental.setPicture(pictureUrl);


        // Save the rental and return the DTO
        return rentalMapper.toDto(rentalRepository.save(rental));
    }

    @Override
    @Transactional
    public Optional<RentalDTO> updateRental(Long id, UpdateRentalRequest request) {
        return rentalRepository.findById(id).map(rental -> {
            // MapStruct automatically updates only non-null fields from the request
            rentalMapper.updateFromRequest(request, rental);

            // Handle picture update if a new file is provided
            if (request.picture() != null && !request.picture().isEmpty()) {
                String pictureUrl = fileStorageService.storeFile(request.picture());
                rental.setPicture(pictureUrl);
            }

            // Save the updated rental (updatedAt is auto-updated by @UpdateTimestamp)
            Rental updatedRental = rentalRepository.save(rental);
            return rentalMapper.toDto(updatedRental);
        });
    }
}

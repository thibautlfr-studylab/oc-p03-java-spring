package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
import com.openclassrooms.chatop.api.exception.BusinessValidationException;
import com.openclassrooms.chatop.api.mapper.RentalMapper;
import com.openclassrooms.chatop.api.model.Rental;
import com.openclassrooms.chatop.api.model.User;
import com.openclassrooms.chatop.api.repository.RentalRepository;
import com.openclassrooms.chatop.api.service.interfaces.IFileStorageService;
import com.openclassrooms.chatop.api.service.interfaces.IRentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for rental operations.
 * Handles business logic for rental-related functionality.
 * Uses MapStruct's RentalMapper for entity-DTO conversions.
 */
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final IFileStorageService fileStorageService;

    @Override
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RentalDTO> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(RentalMapper.INSTANCE::toDto);
    }

    @Override
    @Transactional
    public void createRental(CreateRentalRequest request, MultipartFile picture, User owner) {
        // Validate picture is provided (required for creation)
        if (picture == null || picture.isEmpty()) {
            throw new BusinessValidationException("An image is required to create a rental listing");
        }

        // Store the picture and get its URL (validation happens in FileStorageService)
        String pictureUrl = fileStorageService.storeFile(picture);

        // Create rental entity
        Rental rental = new Rental();
        rental.setName(request.name());
        rental.setSurface(request.surface());
        rental.setPrice(request.price());
        rental.setPicture(pictureUrl);
        rental.setDescription(request.description());
        rental.setOwner(owner);

        // Save
        rentalRepository.save(rental);
    }

    @Override
    @Transactional
    public Optional<RentalDTO> updateRental(Long id, UpdateRentalRequest request, MultipartFile picture) {
        return rentalRepository.findById(id).map(rental -> {
            // Validate that at least one field is being updated
            boolean requestName = request.name() != null && !request.name().isEmpty();

            boolean hasUpdate = requestName ||
                    request.surface() != null ||
                    request.price() != null ||
                    request.description() != null ||
                    (picture != null && !picture.isEmpty());

            if (!hasUpdate) {
                throw new BusinessValidationException("At least one field must be provided for the update");
            }

            // Update fields if provided
            if (requestName) {
                rental.setName(request.name());
            }
            if (request.surface() != null) {
                rental.setSurface(request.surface());
            }
            if (request.price() != null) {
                rental.setPrice(request.price());
            }
            if (request.description() != null) {
                rental.setDescription(request.description());
            }

            // Update the picture if provided (validation happens in FileStorageService)
            if (picture != null && !picture.isEmpty()) {
                String pictureUrl = fileStorageService.storeFile(picture);
                rental.setPicture(pictureUrl);
            }

            // Save and return
            Rental updatedRental = rentalRepository.save(rental);
            return RentalMapper.INSTANCE.toDto(updatedRental);
        });
    }
}

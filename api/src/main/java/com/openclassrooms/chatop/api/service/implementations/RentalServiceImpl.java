package com.openclassrooms.chatop.api.service.implementations;

import com.openclassrooms.chatop.api.dto.request.RentalRequest.CreateRentalRequest;
import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.dto.request.RentalRequest.UpdateRentalRequest;
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
 */
@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements IRentalService {

    private final RentalRepository rentalRepository;
    private final IFileStorageService fileStorageService;

    @Override
    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll().stream()
                .map(RentalDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RentalDTO> getRentalById(Long id) {
        return rentalRepository.findById(id)
                .map(RentalDTO::fromEntity);
    }

    @Override
    @Transactional
    public RentalDTO createRental(CreateRentalRequest request, MultipartFile picture, User owner) {
        // Store the picture and get its URL
        String pictureUrl = fileStorageService.storeFile(picture);

        // Create rental entity
        Rental rental = new Rental();
        rental.setName(request.name());
        rental.setSurface(request.surface());
        rental.setPrice(request.price());
        rental.setPicture(pictureUrl);
        rental.setDescription(request.description());
        rental.setOwner(owner);

        // Save and return
        Rental savedRental = rentalRepository.save(rental);
        return RentalDTO.fromEntity(savedRental);
    }

    @Override
    @Transactional
    public Optional<RentalDTO> updateRental(Long id, UpdateRentalRequest request, MultipartFile picture) {
        return rentalRepository.findById(id).map(rental -> {
            // Update fields if provided
            if (request.name() != null && !request.name().isEmpty()) {
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

            // Update picture if provided
            if (picture != null && !picture.isEmpty()) {
                String pictureUrl = fileStorageService.storeFile(picture);
                rental.setPicture(pictureUrl);
            }

            // Save and return
            Rental updatedRental = rentalRepository.save(rental);
            return RentalDTO.fromEntity(updatedRental);
        });
    }
}

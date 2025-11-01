package com.openclassrooms.chatop.api.repository;

import com.openclassrooms.chatop.api.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Rental entity.
 * Provides CRUD operations for rental management.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    /**
     * Find all rentals with their owners loaded.
     * This prevents N+1 query problem when converting to DTOs by fetching
     * the owner relationship in a single SQL query using JOIN FETCH.
     *
     * @return List of rentals with owners loaded
     */
    @Query("SELECT r FROM Rental r JOIN FETCH r.owner")
    List<Rental> findAllWithOwner();

    /**
     * Find a rental by ID with its owner loaded.
     * This prevents N+1 query problem when converting to DTO by fetching
     * the owner relationship in a single SQL query using JOIN FETCH.
     *
     * @param id Rental ID
     * @return Optional rental with owner loaded
     */
    @Query("SELECT r FROM Rental r JOIN FETCH r.owner WHERE r.id = :id")
    Optional<Rental> findByIdWithOwner(@Param("id") Long id);
}

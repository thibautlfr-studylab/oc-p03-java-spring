package com.openclassrooms.chatop.api.repository;

import com.openclassrooms.chatop.api.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Rental entity.
 * Provides CRUD operations for rental management.
 */
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}

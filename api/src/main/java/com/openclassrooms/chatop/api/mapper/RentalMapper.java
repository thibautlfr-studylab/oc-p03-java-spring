package com.openclassrooms.chatop.api.mapper;

import com.openclassrooms.chatop.api.dto.RentalDTO;
import com.openclassrooms.chatop.api.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for Rental entity to RentalDTO conversions.
 * This interface automatically generates the implementation at compile time.
 */
@Mapper
public interface RentalMapper {

    RentalMapper INSTANCE = Mappers.getMapper(RentalMapper.class);

    /**
     * Convert a Rental entity to a RentalDTO.
     * MapStruct automatically maps fields with matching names.
     * Custom mappings:
     * - owner.id -> owner_id (extracts owner ID from the User relationship)
     * - createdAt -> created_at
     * - updatedAt -> updated_at
     *
     * @param rental the Rental entity to convert
     * @return a RentalDTO containing the rental's information
     */
    @Mapping(source = "owner.id", target = "owner_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    RentalDTO toDto(Rental rental);

    /**
     * Convert a RentalDTO to a Rental entity.
     * Note: This doesn't set the owner relationship, which should be handled separately.
     *
     * @param rentalDTO the RentalDTO to convert
     * @return a Rental entity
     */
    @Mapping(target = "owner", ignore = true)
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    Rental toEntity(RentalDTO rentalDTO);
}

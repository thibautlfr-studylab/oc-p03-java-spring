package com.openclassrooms.chatop.api.mapper;

import com.openclassrooms.chatop.api.dto.MessageDTO;
import com.openclassrooms.chatop.api.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for Message entity to MessageDTO conversions.
 * This interface automatically generates the implementation at compile time.
 */
@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    /**
     * Convert a Message entity to a MessageDTO.
     * MapStruct automatically maps fields with matching names.
     * Custom mappings:
     * - rental.id -> rental_id (extracts rental ID from the Rental relationship)
     * - user.id -> user_id (extracts user ID from the User relationship)
     * - createdAt -> created_at
     * - updatedAt -> updated_at
     *
     * @param message the Message entity to convert
     * @return a MessageDTO containing the message information
     */
    @Mapping(source = "rental.id", target = "rental_id")
    @Mapping(source = "user.id", target = "user_id")
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    MessageDTO toDto(Message message);

    /**
     * Convert a MessageDTO to a Message entity.
     * Note: This doesn't set the rental and user relationships, which should be handled separately.
     *
     * @param messageDTO the MessageDTO to convert
     * @return a Message entity
     */
    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    Message toEntity(MessageDTO messageDTO);
}

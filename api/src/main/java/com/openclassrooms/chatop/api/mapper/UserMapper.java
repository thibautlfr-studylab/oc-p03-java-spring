package com.openclassrooms.chatop.api.mapper;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for User entity to UserDTO conversions.
 * This interface automatically generates the implementation at compile time.
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Convert a User entity to a UserDTO.
     * MapStruct automatically maps fields with matching names.
     * Field name transformations: createdAt -> created_at, updatedAt -> updated_at
     *
     * @param user the User entity to convert
     * @return a UserDTO containing the user's public information
     */
    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UserDTO toDto(User user);

    /**
     * Convert a UserDTO to a User entity.
     * Useful for updates, but note that password field won't be mapped (ignored for security).
     *
     * @param userDTO the UserDTO to convert
     * @return a User entity
     */
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    User toEntity(UserDTO userDTO);
}

package com.openclassrooms.chatop.api.mapper;

import com.openclassrooms.chatop.api.dto.UserDTO;
import com.openclassrooms.chatop.api.dto.request.AuthRequest;
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

    // -----------------------------
    // Entity -> DTO
    // -----------------------------

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    UserDTO toDto(User user);

    // -----------------------------
    // DTO -> Entity
    // -----------------------------

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Password should be set separately after hashing
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(AuthRequest.RegisterRequest request);
}

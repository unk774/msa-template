package ru.imageprocessing.auth.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.imageprocessing.auth.api.dto.RegisterUserRequest;
import ru.imageprocessing.auth.api.dto.UserResponse;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface RegisterUserRequestMapper {
    UserResponse toResponse(RegisterUserRequest request);

    @AfterMapping
    default void afterMapping(RegisterUserRequest request, @MappingTarget UserResponse response) {
        response.setCreatedAt(Date.from(Instant.now()));
    }
}
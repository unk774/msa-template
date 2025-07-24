package ru.imageprocessing.auth.mapper;

import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.imageprocessing.auth.api.dto.UserResponse;

import java.time.Instant;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface UserRepresentationMapper {

    @Mapping(target = "login", source = "username")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "createdAt", source = "createdTimestamp", qualifiedByName = "timestampToDate")
    UserResponse toUserResponse(UserRepresentation request);

    @Named("timestampToDate")
    static Date timestampToDate(Long timestamp) {
        return timestamp != null ?
                Date.from(Instant.ofEpochMilli(timestamp)) :
                null;
    }
}

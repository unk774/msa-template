package org.template.auth.service;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.template.auth.api.dto.RegisterUserRequest;
import org.template.auth.api.dto.UserResponse;
import org.template.auth.mapper.UserRepresentationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegistry {

    private final Keycloak keycloak;
    private final UserRepresentationMapper userRepresentationMapper;

    @Value("${keycloak.realm}")
    private String realm;

    public Integer createUser(RegisterUserRequest registerUserRequest) {
        // User representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerUserRequest.getLogin());
        user.setFirstName(registerUserRequest.getFirstName());
        user.setLastName(registerUserRequest.getLastName());
        user.setEmail(registerUserRequest.getEmail());
        user.setEnabled(true);

        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Create user
        try (Response response = usersResource.create(user)) {

            if (response.getStatus() == 201) {
                String userId = response.getLocation().getPath()
                        .replaceAll(".*/([^/]+)$", "$1");

                // Set password
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(registerUserRequest.getPassword());
                credential.setTemporary(false);

                usersResource.get(userId).resetPassword(credential);
                log.info("User created successfully with ID: " + userId);
            }

            return response.getStatus();
        }
    }

    @Cacheable("userInfo")
    public UserResponse getUserByLogin(String login) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // get by login
        List<UserRepresentation> users = usersResource.searchByUsername(login, true);

        if (users.isEmpty()) {
            throw new NotFoundException("User with login " + login + " not found");
        }

        UserRepresentation user = users.get(0);

        // get attributes
        UserRepresentation fullUser = usersResource.get(user.getId()).toRepresentation();

        return userRepresentationMapper.toUserResponse(fullUser);
    }
}

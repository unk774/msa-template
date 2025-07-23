package ru.imageprocessing.auth.service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.imageprocessing.auth.api.dto.RegisterUserRequest;

@Service
@RequiredArgsConstructor
public class UserRegistry {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public void createUser(RegisterUserRequest registerUserRequest) {
        // User representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerUserRequest.getLogin());
        user.setFirstName(registerUserRequest.getPassword());
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

                System.out.println("User created successfully with ID: " + userId);
            } else {
                System.out.println("Failed to create user. Status: " + response.getStatus());
            }
        }
    }
}

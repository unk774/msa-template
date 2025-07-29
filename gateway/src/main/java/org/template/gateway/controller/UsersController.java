package org.template.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.template.gateway.feign.UserRegistryClient;
import org.template.auth.api.UsersApi;
import org.template.auth.api.dto.RegisterUserRequest;
import org.template.auth.api.dto.UserResponse;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UsersController implements UsersApi {

    private final UserRegistryClient userRegistryClient;

    @Override
    public ResponseEntity<UserResponse> getUserByLogin(String login) {
        return userRegistryClient.getUserByLogin(login);
    }

    @Override
    public ResponseEntity<UserResponse> registerUser(RegisterUserRequest registerUserRequest) {
        return userRegistryClient.registerUser(registerUserRequest);
    }
}

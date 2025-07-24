package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.UsersApi;
import ru.imageprocessing.auth.api.dto.RegisterUserRequest;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.gateway.feign.UserRegistryClient;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UsersController implements UsersApi {

    private final UserRegistryClient userRegistryClient;

    @Override
    public ResponseEntity<UserResponse> apiV1UsersRegisterPost(RegisterUserRequest registerUserRequest) {
        return userRegistryClient.apiV1UsersRegisterPost(registerUserRequest);
    }
}

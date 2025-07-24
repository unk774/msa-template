package ru.imageprocessing.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.UsersApi;
import ru.imageprocessing.auth.api.dto.RegisterUserRequest;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.auth.mapper.RegisterUserRequestMapper;
import ru.imageprocessing.auth.service.UserRegistry;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UsersController implements UsersApi {
    private final UserRegistry userRegistry;
    private final RegisterUserRequestMapper registerUserRequestMapper;

    @Override
    public ResponseEntity<UserResponse> getUserByLogin(String login) {
        return ResponseEntity.ok(userRegistry.getUserByLogin(login));
    }

    @Override
    public ResponseEntity<UserResponse> registerUser(RegisterUserRequest registerUserRequest) {
        Integer response = userRegistry.createUser(registerUserRequest);
        if (response != 201) {
            return ResponseEntity.status(response).build();
        }
        return ResponseEntity.ok(registerUserRequestMapper.toResponse(registerUserRequest));
    }
}

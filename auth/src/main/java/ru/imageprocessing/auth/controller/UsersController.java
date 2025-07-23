package ru.imageprocessing.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.UsersApi;
import ru.imageprocessing.auth.api.dto.RegisterUserRequest;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.auth.mapper.UserResponseMapper;
import ru.imageprocessing.auth.service.UserRegistry;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class UsersController implements UsersApi {
    private final UserRegistry userRegistry;
    private final UserResponseMapper userResponseMapper;

    @Override
    public ResponseEntity<UserResponse> usersRegisterPost(RegisterUserRequest registerUserRequest) {
        Integer response = userRegistry.createUser(registerUserRequest);
        if (response != 201) {
            return ResponseEntity.status(response).build();
        }
        return ResponseEntity.ok(userResponseMapper.toResponse(registerUserRequest));
    }
}

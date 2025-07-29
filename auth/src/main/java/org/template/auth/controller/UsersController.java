package org.template.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.template.auth.mapper.RegisterUserRequestMapper;
import org.template.auth.service.UserRegistry;
import org.template.auth.api.UsersApi;
import org.template.auth.api.dto.RegisterUserRequest;
import org.template.auth.api.dto.UserResponse;

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

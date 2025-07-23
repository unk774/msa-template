package ru.imageprocessing.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.AuthApi;
import ru.imageprocessing.auth.api.dto.AuthResponse;
import ru.imageprocessing.auth.api.dto.LoginRequest;
import ru.imageprocessing.auth.api.dto.RefreshTokenRequest;
import ru.imageprocessing.auth.api.dto.TokenValidationResponse;
import ru.imageprocessing.auth.api.dto.ValidateTokenRequest;
import ru.imageprocessing.auth.service.Auth;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class AuthController implements AuthApi {

    private final Auth auth;

    @Override
    public ResponseEntity<AuthResponse> authLoginPost(LoginRequest loginRequest) {
        return ResponseEntity.ok(auth.login(loginRequest));
    }

    @Override
    public ResponseEntity<AuthResponse> authRefreshPost(RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(auth.refreshToken(refreshTokenRequest.getRefreshToken()));
    }
}

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

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController implements AuthApi {
    @Override
    public ResponseEntity<AuthResponse> authLoginPost(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public ResponseEntity<AuthResponse> authRefreshPost(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TokenValidationResponse> authValidatePost(ValidateTokenRequest validateTokenRequest) {
        return null;
    }
}

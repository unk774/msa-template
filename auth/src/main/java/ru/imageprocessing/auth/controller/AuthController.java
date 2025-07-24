package ru.imageprocessing.auth.controller;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.AuthApi;
import ru.imageprocessing.auth.api.dto.AuthResponse;
import ru.imageprocessing.auth.api.dto.LoginRequest;
import ru.imageprocessing.auth.api.dto.RefreshTokenRequest;
import ru.imageprocessing.auth.service.Auth;

@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthController implements AuthApi {

    private final Auth auth;

    @Override
    public ResponseEntity<AuthResponse> getToken(LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok(auth.login(loginRequest));
        } catch (NotAuthorizedException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public ResponseEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(auth.refreshToken(refreshTokenRequest.getRefreshToken()));
    }
}

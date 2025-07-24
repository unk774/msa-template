package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.auth.api.AuthApi;
import ru.imageprocessing.auth.api.dto.AuthResponse;
import ru.imageprocessing.auth.api.dto.LoginRequest;
import ru.imageprocessing.auth.api.dto.RefreshTokenRequest;
import ru.imageprocessing.gateway.feign.AuthClient;

@RequiredArgsConstructor
@Slf4j
@RestController
public class AuthController implements AuthApi {

    private final AuthClient authClient;

    @Override
    public ResponseEntity<AuthResponse> getToken(LoginRequest loginRequest) {
        return authClient.getToken(loginRequest);
    }

    @Override
    public ResponseEntity<AuthResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return authClient.refreshToken(refreshTokenRequest);
    }
}

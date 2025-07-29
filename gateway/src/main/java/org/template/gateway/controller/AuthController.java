package org.template.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.template.gateway.feign.AuthClient;
import org.template.auth.api.AuthApi;
import org.template.auth.api.dto.AuthResponse;
import org.template.auth.api.dto.LoginRequest;
import org.template.auth.api.dto.RefreshTokenRequest;

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

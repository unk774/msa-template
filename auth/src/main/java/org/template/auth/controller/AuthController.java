package org.template.auth.controller;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.template.auth.service.Auth;
import org.template.auth.api.AuthApi;
import org.template.auth.api.dto.AuthResponse;
import org.template.auth.api.dto.LoginRequest;
import org.template.auth.api.dto.RefreshTokenRequest;

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

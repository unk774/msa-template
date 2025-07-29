package ru.imageprocessing.auth;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import ru.imageprocessing.auth.api.dto.AuthResponse;
import ru.imageprocessing.auth.api.dto.LoginRequest;
import ru.imageprocessing.auth.service.Auth;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class AuthIntegrationTest extends KeyCloakIntegrationTest{

    private final Auth auth;

    @Test
    void loginAndRefresh_IntegrationTest() {
        LoginRequest request = new LoginRequest("administrator", "administrator");

        AuthResponse loginResponse = auth.login(request);
        AuthResponse refreshResponse = auth.refreshToken(loginResponse.getRefreshToken());

        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(refreshResponse.getAccessToken());
        assertNotEquals(loginResponse.getAccessToken(), refreshResponse.getAccessToken());
    }
}
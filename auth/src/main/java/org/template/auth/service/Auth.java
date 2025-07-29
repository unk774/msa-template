package org.template.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.template.auth.api.dto.AuthResponse;
import org.template.auth.api.dto.LoginRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class Auth {

    private final RestTemplate restTemplate;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public AuthResponse login(LoginRequest loginRequest) {
        try (Keycloak keycloakClient = KeycloakBuilder.builder()
                     .serverUrl(authServerUrl)
                     .realm(realm)
                     .scope("openid")
                     .grantType(OAuth2Constants.PASSWORD)
                     .clientId(clientId)
                     .username(loginRequest.getUsername())
                     .password(loginRequest.getPassword())
                     .clientSecret(URLEncoder.encode(clientSecret, StandardCharsets.UTF_8))
                     .build()) {

            AccessTokenResponse tokenResponse = keycloakClient.tokenManager().getAccessToken();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(tokenResponse.getToken());
            authResponse.setRefreshToken(tokenResponse.getRefreshToken());
            authResponse.setExpiresIn(tokenResponse.getExpiresIn());
            authResponse.setTokenType(tokenResponse.getTokenType());
            return authResponse;
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token", authServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    tokenUrl,
                    requestEntity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken((String) responseBody.get("access_token"));
            authResponse.setRefreshToken((String) responseBody.get("refresh_token"));
            authResponse.setExpiresIn(Long.parseLong(responseBody.get("expires_in").toString()));
            authResponse.setTokenType((String) responseBody.get("token_type"));


            return authResponse;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Token refresh failed: " + e.getResponseBodyAsString(), e);
        }
    }
}
